package org.deco.gachicoding.user.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.service.AuthService;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.user.dto.request.LoginRequestDto;
import org.deco.gachicoding.user.dto.response.UserResponseDto;
import org.deco.gachicoding.user.dto.request.UserSaveRequestDto;
import org.deco.gachicoding.user.dto.request.UserUpdateRequestDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * {@link Transactional} : 아이디 중복 시 Transaction silently rolled back because it has been marked as rollback-only 발생
     * <br> 원인 : 트랜잭션은 재사용 될수 없다.
     * <br> <br> save하면서 같은 이메일이 있으면 예외를 발생, 예외 발생 시 기본 값으로 들어있는 롤백이 true가 됨. save가 끝나고 나오면서 registerUser로 돌아 왔을때 @Transactional어노테이션이 있으면
     * 커밋을 앞에서 예외를 잡았기 때문에 문제 없다고 판단, 커밋을 실행한다. 하지만 roll-back only**이 마킹되어 있어 **롤백함.
     * <br> <br> 트러블 슈팅으로 넣으면 좋을 듯
     */
    public Long registerUser(UserSaveRequestDto dto) {

        // 이메일 중복 체크
        String registerEmail = dto.getUserEmail();
        if (isDuplicatedEmail(registerEmail))
            throw new DataIntegrityViolationException("중복된 이메일 입니다.");
        // 비밀번호 변조
        dto.encryptPassword(passwordEncoder);
        // 유저 저장
        Long userIdx = userRepository.save(dto.toEntity()).getUserIdx();

        // 유저 이메일로 인증 메일 보내기

        return userIdx;
    }

    @Transactional
    public UserResponseDto login(LoginRequestDto requestDto, HttpSession httpSession) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));

            UserDetails principal = (User) authentication.getPrincipal();

            log.info("유저 이메일 > " + principal.getUsername());
            log.info("유저 비밀번호 > " + principal.getPassword());

            Optional<User> user = userRepository.findByUserEmail(principal.getUsername());
            UserResponseDto userResponseDto = new UserResponseDto(user.get());
            httpSession.setAttribute("user", userResponseDto);

            return userResponseDto;
            // BadCredentialsException - 스프링 시큐리티 에서 아이디 또는 비밀번호가 틀렸을 경우 나오는 예외
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            return new UserResponseDto(null);
        }
    }


    @Transactional
    public Long updateUser(Long idx, UserUpdateRequestDto dto) {

        User user = userRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다. 회원 번호 = " + idx));

        user.update(dto.getUserNick(), dto.getUserPassword(), dto.isUserLocked(), dto.isUserEnabled());

        return idx;
    }

    @Transactional
    public Long deleteUser(Long idx) {
        userRepository.deleteById(idx);
        return idx;
    }

    @Transactional
    public boolean isDuplicatedEmail(String userEmail) {
        return getUserByUserEmail(userEmail).isPresent();
    }

    @Transactional
    public Optional<User> getUserByUserEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserDetails userDetails = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("등록되지 않은 사용자 입니다"));

        return userDetails;
    }
}