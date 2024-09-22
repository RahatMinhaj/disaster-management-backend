package com.disaster.managementsystem.component.error.handler;

import com.disaster.managementsystem.component.error.ErrorHandlingProperties;
import com.disaster.managementsystem.component.error.mapper.ErrorCodeMapper;
import com.disaster.managementsystem.component.error.mapper.ErrorMessageMapper;
import com.disaster.managementsystem.component.error.mapper.HttpStatusMapper;
import com.disaster.managementsystem.support.ApiErrorResponseDto;
import com.disaster.managementsystem.support.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;


public class HttpMessageNotReadableApiExceptionHandler extends AbstractApiExceptionHandler {
    public HttpMessageNotReadableApiExceptionHandler(ErrorHandlingProperties properties,
                                                     HttpStatusMapper httpStatusMapper,
                                                     ErrorCodeMapper errorCodeMapper,
                                                     ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof HttpMessageNotReadableException;
    }

    @Override
    public ApiErrorResponseDto handle(Throwable exception) {
        return ApiErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(getErrorMessage(exception))
                .error(ErrorDto.builder().code(getErrorCode(exception)).build())
                .build();
    }

}
