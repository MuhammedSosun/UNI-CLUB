package com.uniClub.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

@Slf4j
@Component
public class MDCLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper cachedResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        try {
            MDCUtil.setUseContext();
            MDC.put("method", request.getMethod());
            MDC.put("path", request.getRequestURI());

            logRequest(cachedRequest);

            filterChain.doFilter(cachedRequest, cachedResponse);

            logResponse(cachedResponse, System.currentTimeMillis() - startTime);

        } finally {
            cachedResponse.copyBodyToResponse();

            MDC.clear();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String requestBody = getRequestBody(request);
        StringBuilder headers = new StringBuilder();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            headers.append(header).append("=").append(request.getHeader(header)).append(", ");
        }

        log.info("""
                REQUEST
                traceId={} | method={} | uri={} | ip={} 
                headers=[{}]
                body={}
                """,
                MDCUtil.getTraceId(),
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr(),
                headers,
                requestBody);
    }

    private void logResponse(ContentCachingResponseWrapper response, long durationMs) {
        String responseBody = getResponseBody(response);
        log.info("""
                RESPONSE
                traceId={} | status={} | duration={}ms 
                body={}
                """,
                MDCUtil.getTraceId(),
                response.getStatus(),
                durationMs,
                responseBody);
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] buf = request.getContentAsByteArray();
        if (buf.length == 0) return "(empty)";
        return new String(buf, StandardCharsets.UTF_8);
    }
    // bu iki method cachlenmis veriyi byte dizisinden stringe çevirir
    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] buf = response.getContentAsByteArray();
        if (buf.length == 0) return "(empty)";
        return new String(buf, StandardCharsets.UTF_8);
    }
}
