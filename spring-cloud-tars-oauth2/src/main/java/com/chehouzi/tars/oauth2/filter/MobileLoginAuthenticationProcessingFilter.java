package com.chehouzi.tars.oauth2.filter;

import com.chehouzi.tars.oauth2.authentication.MobileAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author twg
 * @since 2019/4/19
 */
@Slf4j
public class MobileLoginAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private boolean postOnly = true;
    private final RedisTemplate redisTemplate;

    public MobileLoginAuthenticationProcessingFilter(RedisTemplate redisTemplate, String defaultFilterProcessesUrl){
        super(defaultFilterProcessesUrl);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if(this.postOnly && !HttpMethod.POST.matches(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String username = request.getParameter("mobile");
            String password = request.getParameter("password");
            if(Objects.isNull(username)) {
                throw new AuthenticationServiceException("mobile must required");
            } else if(Objects.isNull(password)) {
                throw new AuthenticationServiceException("password must required");
            } else {
                MobileAuthenticationToken authRequest = new MobileAuthenticationToken(username, password);
                Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);
                if(Objects.nonNull(authentication)) {
                    Map map = (Map) authentication.getDetails();
                    this.redisTemplate.opsForValue().set(map.get("token"), map, 300, TimeUnit.SECONDS);
                }
                return authentication;
            }
        }
    }
}
