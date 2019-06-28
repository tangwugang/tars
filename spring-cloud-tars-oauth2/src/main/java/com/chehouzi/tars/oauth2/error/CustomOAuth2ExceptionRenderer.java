package com.chehouzi.tars.oauth2.error;

import com.alibaba.fastjson.JSON;
import com.chehouzi.tars.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.provider.error.OAuth2ExceptionRenderer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;

/**
 * @author twg
 * @since 2019/4/3
 */
@Slf4j
public class CustomOAuth2ExceptionRenderer implements OAuth2ExceptionRenderer {

    @Override
    public void handleHttpEntityResponse(HttpEntity<?> responseEntity, ServletWebRequest webRequest) throws Exception {
        if (responseEntity == null) {
            return;
        }
        HttpStatus status = ((ResponseEntity<?>) responseEntity).getStatusCode();
        HttpOutputMessage outputMessage = createHttpOutputMessage(webRequest);
        if (responseEntity instanceof ResponseEntity && outputMessage instanceof ServerHttpResponse) {
            ((ServerHttpResponse) outputMessage).setStatusCode(((ResponseEntity<?>) responseEntity).getStatusCode());
        }
        if (log.isDebugEnabled()) {
            log.debug("OAuth2Exception {} {} ", status.value(), status.getReasonPhrase());
        }
        outputMessage.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        outputMessage.getBody().write(JSON.toJSONString(Result.buildErrorResult(status.value(),status.getReasonPhrase())).getBytes(Charset.defaultCharset()));
    }


    private HttpOutputMessage createHttpOutputMessage(NativeWebRequest webRequest) throws Exception {
        HttpServletResponse servletResponse = (HttpServletResponse) webRequest.getNativeResponse();
        return new ServletServerHttpResponse(servletResponse);
    }
}
