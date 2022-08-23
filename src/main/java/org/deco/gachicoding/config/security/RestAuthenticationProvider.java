package org.deco.gachicoding.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
public class RestAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String loginEmail = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        log.info("인증 시도 유저 이메일 - {}, 인증 시도 유저 비밀번호 - {}", loginEmail, password);

        UserDetails userDto = userDetailsService.loadUserByUsername(loginEmail);

        if (!passwordEncoder.matches(password, userDto.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new RestAuthenticationToken(userDto, null, userDto.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(RestAuthenticationToken.class);
    }
}
