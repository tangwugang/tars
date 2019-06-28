package com.chehouzi.tars.oauth2.web.filter;

import com.chehouzi.tars.core.configuration.IgnoreUrlsConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 自定义cookie 认证
 *
 * @author twg
 * @since 2019/4/13
 */
@Slf4j
public abstract class AbstractCustomCookieAuthenticationFilter extends GenericFilterBean {
    private final IgnoreUrlsConfiguration ignoreUrls;
    private final RedisTemplate redisTemplate;
    private RememberMeServices rememberMeServices = new NullRememberMeServices();
    private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();
    private boolean isMatches = false;
    /**
     * 过滤请求URL的cookie校验
     */
    private String requestPath = "(/|/login)+?";

    protected AbstractCustomCookieAuthenticationFilter(RedisTemplate redisTemplate, IgnoreUrlsConfiguration urls) {
        this.redisTemplate = redisTemplate;
        this.ignoreUrls = urls;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        List<String> urls = ignoreUrls.getAnon();
        for (String url : urls) {
            if (url.matches(request.getRequestURI())) {
                isMatches = true;
                break;
            }
        }
        handler(request, response, chain);
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        rememberMeServices.loginFail(request, response);
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    protected void handler(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (isAuthenticateHeader(header) && !isMatches &&
                !request.getRequestURI().matches(requestPath)) {
            Cookie cookie = WebUtils.getCookie(request, "USERTOKEN");
            if (Objects.isNull(cookie) || !redisTemplate.hasKey(cookie.getValue())) {
                log.error("Request URI {} does not have Unauthorized. Because the User Token in Cookie is invalid", request.getRequestURI());
                unsuccessfulAuthentication(request, response, new CookieTheftException("User Token in Cookie is invalid"));
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isAuthenticateHeader(String header) {
        return header == null || !header.toLowerCase().startsWith("basic ") ||
                !header.toLowerCase().startsWith("bearer ");
    }

    public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }
}
