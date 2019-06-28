package com.chehouzi.tars.oauth2.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author twg
 * @since 2019/4/7
 */
@Slf4j
public class UserLoginAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private boolean postOnly = true;
    private final RedisTemplate redisTemplate;

    public UserLoginAuthenticationProcessingFilter(RedisTemplate redisTemplate, String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (this.postOnly && !HttpMethod.POST.matches(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (Objects.isNull(username)) {
            throw new AuthenticationServiceException("username must required");
        }
        if (Objects.isNull(password)) {
            throw new AuthenticationServiceException("password must required");
        }
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);
        Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);
        if (Objects.nonNull(authentication)) {
            if (authentication.getDetails() instanceof Map) {
                Map userMap = (Map) authentication.getDetails();
                Cookie cookie = new Cookie("USERTOKEN", (String) userMap.get("token"));
                cookie.setSecure(request.isSecure());
                LocalDateTime now = LocalDateTime.now();
                int expire = (int) (now.plusDays(1).toEpochSecond(ZoneOffset.UTC) - now.toEpochSecond(ZoneOffset.UTC));
                cookie.setMaxAge(expire);
                response.addCookie(cookie);
                redisTemplate.opsForValue().set(cookie.getValue(), userMap, expire, TimeUnit.SECONDS);
            }
        }
        return authentication;

    }
}
