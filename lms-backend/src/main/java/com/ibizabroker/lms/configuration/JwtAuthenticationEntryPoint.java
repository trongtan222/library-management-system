package com.ibizabroker.lms.configuration; // <-- đổi cho khớp package của bạn

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.Serializable;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // Return 401 with JSON body for invalid/missing JWT
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        String message = (authException != null && authException.getMessage() != null)
                ? authException.getMessage()
                : "Unauthorized";
        String body = "{\"status\":\"error\",\"error\":\"" + message.replace("\"", "\\\"") + "\"}";
        ServletOutputStream out = response.getOutputStream();
        out.write(body.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        out.flush();
    }
}
