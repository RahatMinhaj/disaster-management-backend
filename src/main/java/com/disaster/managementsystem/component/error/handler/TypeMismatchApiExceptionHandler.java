package com.disaster.managementsystem.component.error.handler;

import com.disaster.managementsystem.component.error.ErrorHandlingProperties;
import com.disaster.managementsystem.component.error.mapper.ErrorCodeMapper;
import com.disaster.managementsystem.component.error.mapper.ErrorMessageMapper;
import com.disaster.managementsystem.component.error.mapper.HttpStatusMapper;
import com.disaster.managementsystem.support.ApiErrorResponseDto;
import com.disaster.managementsystem.support.ErrorDto;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public class TypeMismatchApiExceptionHandler extends AbstractApiExceptionHandler {
    public TypeMismatchApiExceptionHandler(ErrorHandlingProperties properties,
                                           HttpStatusMapper httpStatusMapper,
                                           ErrorCodeMapper errorCodeMapper,
                                           ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof TypeMismatchException;
    }

    @Override
    public ApiErrorResponseDto handle(Throwable exception) {
        ApiErrorResponseDto response = ApiErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(getErrorMessage(exception))
                .error(ErrorDto.builder().code(getErrorCode(exception)).build())
                .build();

        TypeMismatchException ex = (TypeMismatchException) exception;
        response.getError().getProperties().put("property", getPropertyName(ex));
        response.getError().getProperties().put("rejectedValue", ex.getValue());
        response.getError().getProperties().put("expectedType", ex.getRequiredType() != null ? ex.getRequiredType().getName() : null);
        return response;
    }

    private String getPropertyName(TypeMismatchException exception) {
        if (exception instanceof MethodArgumentTypeMismatchException) {
            return ((MethodArgumentTypeMismatchException) exception).getName();
        } else {
            return exception.getPropertyName();
        }
    }
}
