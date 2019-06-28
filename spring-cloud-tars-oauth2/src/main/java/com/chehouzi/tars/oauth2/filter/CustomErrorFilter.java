package com.chehouzi.tars.oauth2.filter;

import com.alibaba.fastjson.JSON;
import com.chehouzi.tars.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author twg
 * @since 2019/4/3
 */
@Slf4j
public class CustomErrorFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.debug("currentUri：{}，statusCode：{}", servletRequest.getAttribute("currentUri"), servletRequest.getAttribute("javax.servlet.error.status_code"));
        }
        if (null != servletRequest.getAttribute("javax.servlet.error.status_code") &&
                HttpStatus.OK.value() != (Integer) servletRequest.getAttribute("javax.servlet.error.status_code")) {
            HttpStatus httpStatus = HttpStatus.valueOf((Integer) servletRequest.getAttribute("javax.servlet.error.status_code"));
            servletResponse.getWriter().write(JSON.toJSONString(Result.buildErrorResult(httpStatus.value(),httpStatus.getReasonPhrase())));
            servletResponse.getWriter().flush();
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
