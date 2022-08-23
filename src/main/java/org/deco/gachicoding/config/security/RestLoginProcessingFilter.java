package org.deco.gachicoding.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.user.dto.request.UserAuthenticationDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class RestLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public RestLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {


        UserAuthenticationDto userDto = objectMapper.readValue(request.getReader(), UserAuthenticationDto.class);

        log.info("이메일 유효성 검사 로직 필요");

        if (!StringUtils.hasText(userDto.getUsername()) || !StringUtils.hasText(userDto.getPassword())) {
            throw new IllegalArgumentException("Username or Password is empty");
        }

        RestAuthenticationToken restAuthenticationToken = new RestAuthenticationToken(userDto.getUsername(), userDto.getPassword());

        return getAuthenticationManager().authenticate(restAuthenticationToken);
    }
}
