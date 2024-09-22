package com.disaster.managementsystem.component.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.disaster.managementsystem.component.error.mapper.ErrorCodeMapper;
import com.disaster.managementsystem.component.error.mapper.ErrorMessageMapper;
import com.disaster.managementsystem.component.error.mapper.HttpStatusMapper;
import com.disaster.managementsystem.support.ApiErrorResponseDto;
import com.disaster.managementsystem.support.ErrorDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class ApiResponseService {
    private final HttpStatusMapper httpStatusMapper;
    private final ErrorCodeMapper errorCodeMapper;
    private final ErrorMessageMapper errorMessageMapper;
    private final ObjectMapper objectMapper;
    private final LoggingService loggingService;

    @Autowired
    public ApiResponseService(HttpStatusMapper httpStatusMapper,
                              ErrorCodeMapper errorCodeMapper,
                              ErrorMessageMapper errorMessageMapper,
                              ObjectMapper objectMapper,
                              LoggingService loggingService) {
        this.httpStatusMapper = httpStatusMapper;
        this.errorCodeMapper = errorCodeMapper;
        this.errorMessageMapper = errorMessageMapper;
        this.objectMapper = objectMapper;
        this.loggingService = loggingService;
    }

    public void prepareResponse(HttpServletResponse httpServletResponse, Exception exception) throws IOException {
        HttpStatusCode httpStatus = httpStatusMapper.getHttpStatus(exception, HttpStatus.UNAUTHORIZED);
        String code = errorCodeMapper.getErrorCode(exception);
        String message = errorMessageMapper.getErrorMessage(exception);
        ApiErrorResponseDto apiErrorResponse = ApiErrorResponseDto.builder()
                .status(httpStatus.value())
                .message(message)
                .error(ErrorDto.builder().code(code).build())
                .build();

        loggingService.logException(apiErrorResponse, exception);

        /*if (Objects.nonNull(apiErrorResponse.getCode())) {
            message = apiService.getMessage(apiErrorResponse.getCode());
            if (Objects.nonNull(message)) {
                apiErrorResponse.setMessage(message);
            }
        }*/

        httpServletResponse.setStatus(apiErrorResponse.getStatus());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(apiErrorResponse));
    }
}
