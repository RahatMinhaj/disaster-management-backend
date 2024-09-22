package com.disaster.managementsystem.component.error.handler;

import com.disaster.managementsystem.component.error.ErrorHandlingProperties;
import com.disaster.managementsystem.component.error.mapper.ErrorCodeMapper;
import com.disaster.managementsystem.component.error.mapper.ErrorMessageMapper;
import com.disaster.managementsystem.component.error.mapper.HttpStatusMapper;
import com.disaster.managementsystem.support.ApiErrorResponseDto;
import com.disaster.managementsystem.support.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

public class ObjectOptimisticLockingFailureApiExceptionHandler extends AbstractApiExceptionHandler {

    public ObjectOptimisticLockingFailureApiExceptionHandler(ErrorHandlingProperties properties,
                                                             HttpStatusMapper httpStatusMapper,
                                                             ErrorCodeMapper errorCodeMapper,
                                                             ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof ObjectOptimisticLockingFailureException;
    }

    @Override
    public ApiErrorResponseDto handle(Throwable exception) {

        ApiErrorResponseDto response =
                ApiErrorResponseDto.builder()
                        .status(getHttpStatus(exception, HttpStatus.CONFLICT).value())
                        .message(exception.getMessage())
                        .error(ErrorDto.builder().code(getErrorCode(exception)).build())
                        .build();

        ObjectOptimisticLockingFailureException ex = (ObjectOptimisticLockingFailureException) exception;
        response.getError().getProperties().put("identifier", ex.getIdentifier());
        response.getError().getProperties().put("persistentClassName", ex.getPersistentClassName());
        return response;
    }
}