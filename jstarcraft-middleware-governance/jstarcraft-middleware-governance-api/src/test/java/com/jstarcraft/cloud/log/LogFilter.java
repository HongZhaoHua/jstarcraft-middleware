package com.jstarcraft.cloud.log;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.jstarcraft.core.utility.StringUtility;

/**
 * 日志过滤器
 * 
 * @author Birdy
 *
 */
public class LogFilter extends OncePerRequestFilter {

    /**
     * 获取内容
     * 
     * @param buffer
     * @param charset
     * @return
     */
    private String getContent(byte[] buffer, String charset) {
        if (buffer == null || buffer.length == 0) {
            return StringUtility.EMPTY;
        }
        try {
            return new String(buffer, charset);
        } catch (Exception exception) {
            throw new RuntimeException("内容编解码异常", exception);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long instant = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            // 请求与响应内容
            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

            logger.debug(StringUtility.format("request url is:{}?{}", request.getRequestURL(), request.getQueryString()));
            String requestBody = this.getContent(wrappedRequest.getContentAsByteArray(), request.getCharacterEncoding());
            if (requestBody.length() > 0) {
                logger.debug(StringUtility.format("request body is:{}", requestBody));
            }

            filterChain.doFilter(wrappedRequest, wrappedResponse);

            logger.debug(StringUtility.format("response status is:{}", response.getStatus()));
            String responseBody = this.getContent(wrappedResponse.getContentAsByteArray(), response.getCharacterEncoding());
            if (responseBody.length() > 0) {
                logger.debug(StringUtility.format("response body is:{}", responseBody));
            }
            wrappedResponse.copyBodyToResponse();
        } else {
            filterChain.doFilter(request, response);
        }
        if (logger.isWarnEnabled()) {
            instant = System.currentTimeMillis() - instant;
            logger.warn(StringUtility.format("handle url [{}] use [{}] ms", request.getRequestURL(), instant));
        }
    }

}
