package com.uniClub.exceptions.handle;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // ApiError formatında manuel JSON yazıyoruz (Jackson kullanarak)
        String json = String.format(
                "{\"status\": 401, \"errorDetail\": {\"path\": \"%s\", \"message\": \"%s\", \"host\": \"server\", \"createTime\": \"%s\"}}",
                request.getRequestURI(), authException.getMessage(), java.time.LocalDateTime.now()
        );

        response.getWriter().write(json);
    }
}
