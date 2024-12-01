package com.is.uno.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.is.uno.dto.SimpleResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // logger.error("Unauthorized error: {}", authException.getMessage());

        // response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // final Map<String, Object> body = new HashMap<>();
        // body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        // body.put("error", "Unauthorized");
        // body.put("message", authException.getMessage());
        // body.put("path", request.getServletPath());

        // final ObjectMapper mapper = new ObjectMapper();
        // mapper.writeValue(response.getOutputStream(), body);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(),
                new SimpleResponse("Вы не авторизованы: " + authException.getMessage(), false));
    }

}
