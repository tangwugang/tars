package com.chehouzi.tars.oauth2.web;

import cn.hutool.core.util.StrUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author twg
 * @since 2019/4/26
 */
public class CustomSimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private String defaultFailureUrl;
    private HttpStatus httpStatus;
    private String errorMsg;
    private boolean forwardToDestination = false;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public CustomSimpleUrlAuthenticationFailureHandler() {
        super();
    }

    public CustomSimpleUrlAuthenticationFailureHandler(HttpStatus httpStatus, String errorMsg) {
        super();
        this.httpStatus = httpStatus;
        this.errorMsg = errorMsg;
    }

    public CustomSimpleUrlAuthenticationFailureHandler(String defaultFailureUrl) {
        super(defaultFailureUrl);
        setDefaultFailureUrl(defaultFailureUrl);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (defaultFailureUrl == null) {
            logger.debug("No failure URL set, sending 401 Unauthorized error");
            response.sendError(httpStatus != null ? httpStatus.value() : HttpStatus.UNAUTHORIZED.value(),
                    StrUtil.isNotBlank(errorMsg) && (StrUtil.isBlank(exception.getMessage())
                            || exception instanceof BadCredentialsException) ? errorMsg : exception.getMessage());
        } else {
            saveException(request, exception);

            if (forwardToDestination) {
                logger.debug("Forwarding to " + defaultFailureUrl);

                request.getRequestDispatcher(defaultFailureUrl)
                        .forward(request, response);
            } else {
                logger.debug("Redirecting to " + defaultFailureUrl);
                redirectStrategy.sendRedirect(request, response, defaultFailureUrl);
            }
        }
    }

    @Override
    public void setDefaultFailureUrl(String defaultFailureUrl) {
        Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultFailureUrl),
                () -> "'" + defaultFailureUrl + "' is not a valid redirect URL");
        this.defaultFailureUrl = defaultFailureUrl;
    }
}
