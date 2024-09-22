package com.disaster.managementsystem.component.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
    private final ApiResponseService apiResponseService;

    @Autowired
    public UnauthorizedEntryPoint(ApiResponseService apiResponseService) {
        this.apiResponseService = apiResponseService;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws JsonProcessingException, IOException {
        apiResponseService.prepareResponse(response, authException);
    }
}
