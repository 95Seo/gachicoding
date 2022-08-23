package org.deco.gachicoding.user.application;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.auth.Auth;
import org.deco.gachicoding.domain.auth.AuthRepository;
import org.deco.gachicoding.service.MailService;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.user.dto.request.UserAuthenticationDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final MailService mailService;

    /**
     * 이메일 인증 토큰 생성
     *
     * @return
     */
    public UUID sendEmailConfirmationToken(String receiverEmail) {

        Optional<Auth> auth = authRepository.findByAuthEmailAndAuthExpdateAfterAndExpiredIsFalse(receiverEmail, LocalDateTime.now());
        Assert.hasText(receiverEmail, "receiverEmail은 필수 입니다.");

        UUID authToken;
        if (auth.isEmpty())
            authToken = authRepository.save(Auth.createEmailConfirmationToken(receiverEmail)).getAuthToken();
        else
            authToken = auth.get().renewToken();

        mailService.sendConfirmMail(receiverEmail, authToken);
        return authToken;
    }

    // 만료 또는 인증된 토큰 처리 필요
    public Auth checkToken(UUID authToken) {
        Optional<Auth> auth = authRepository.findByAuthToken(authToken);
        return auth.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Username"));

//        List<GrantedAuthority> roles = new ArrayList<>();
//        roles.add(new SimpleGrantedAuthority("ROLE_USER")); // 유저 롤을 다시 적용해야함.

        UserDetails userDetails = new UserAuthenticationDto(user.getUserEmail(), user.getUserPassword());

        return userDetails;
    }
}