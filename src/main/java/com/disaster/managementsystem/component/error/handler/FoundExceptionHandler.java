package com.disaster.managementsystem.component.error.handler;

import com.disaster.managementsystem.component.error.exception.DataAlreadyExistsException;
import com.disaster.managementsystem.component.error.exception.FoundException;
import com.disaster.managementsystem.component.error.mapper.ErrorCodeMapper;
import com.disaster.managementsystem.component.error.mapper.ErrorMessageMapper;
import com.disaster.managementsystem.component.error.mapper.HttpStatusMapper;
import com.disaster.managementsystem.support.ApiErrorResponseDto;
import com.disaster.managementsystem.support.ErrorDto;
import org.springframework.http.HttpStatus;

public class FoundExceptionHandler extends AbstractApiExceptionHandler {
    public FoundExceptionHandler(HttpStatusMapper httpStatusMapper,
                                       ErrorCodeMapper errorCodeMapper,
                                       ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof FoundException || exception instanceof DataAlreadyExistsException;
    }

    @Override
    public ApiErrorResponseDto handle(Throwable exception) {
        return ApiErrorResponseDto.builder()
                .status(HttpStatus.FOUND.value())
                .message(getErrorMessage(exception))
                .error(ErrorDto.builder().code(getErrorCode(exception)).build())
                .build();
    }
}