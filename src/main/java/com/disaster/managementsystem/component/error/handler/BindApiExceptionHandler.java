package com.disaster.managementsystem.component.error.handler;

import com.disaster.managementsystem.component.error.ErrorHandlingProperties;
import com.disaster.managementsystem.component.error.mapper.ErrorCodeMapper;
import com.disaster.managementsystem.component.error.mapper.ErrorMessageMapper;
import com.disaster.managementsystem.component.error.mapper.HttpStatusMapper;
import com.disaster.managementsystem.support.ApiErrorResponseDto;
import com.disaster.managementsystem.support.ApiFieldError;
import com.disaster.managementsystem.support.ApiGlobalError;
import com.disaster.managementsystem.support.ErrorDto;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class BindApiExceptionHandler extends AbstractApiExceptionHandler {

    private final ErrorHandlingProperties properties;

    public BindApiExceptionHandler(ErrorHandlingProperties properties,
                                   HttpStatusMapper httpStatusMapper,
                                   ErrorCodeMapper errorCodeMapper,
                                   ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
        this.properties = properties;
    }

    @Override
    public boolean canHandle(Throwable exception) {
        // BindingResult is a common interface between org.springframework.validation.BindException
        // and org.springframework.web.bind.support.WebExchangeBindException
        return exception instanceof BindingResult;
    }

    @Override
    public ApiErrorResponseDto handle(Throwable exception) {

        BindingResult bindingResult = (BindingResult) exception;
        ApiErrorResponseDto response = ApiErrorResponseDto.builder()
                        .status(getHttpStatus(exception, HttpStatus.BAD_REQUEST).value())
                        .message(getMessage(bindingResult))
                        .error(ErrorDto.builder().code(getErrorCode(exception)).build())
                        .build();

        if (bindingResult.hasFieldErrors()) {
            bindingResult.getFieldErrors().stream()
                    .map(fieldError -> ApiFieldError.builder()
                            .code(getCode(fieldError))
                            .property(fieldError.getField())
                            .message(getMessage(fieldError))
                            .rejectedValue(fieldError.getRejectedValue())
                            .path(getPath(fieldError))
                            .build())
                    .forEach(error -> response.getError().getFieldErrors().add(error));
        }

        if (bindingResult.hasGlobalErrors()) {
            bindingResult.getGlobalErrors().stream()
                    .map(globalError -> ApiGlobalError.builder()
                            .code(errorCodeMapper.getErrorCode(globalError.getCode()))
                            .message(errorMessageMapper.getErrorMessage(globalError.getCode(), globalError.getDefaultMessage()))
                            .build())
                    .forEach(error -> response.getError().getGlobalErrors().add(error));
        }

        return response;
    }

    private String getCode(FieldError fieldError) {
        String code = fieldError.getCode();
        String fieldSpecificCode = fieldError.getField() + "." + code;
        return errorCodeMapper.getErrorCode(fieldSpecificCode, code);
    }

    private String getMessage(FieldError fieldError) {
        String code = fieldError.getCode();
        String fieldSpecificCode = fieldError.getField() + "." + code;
        return errorMessageMapper.getErrorMessage(fieldSpecificCode, code, fieldError.getDefaultMessage());
    }

    private String getMessage(BindingResult bindingResult) {
        return "Validation failed for object='" + bindingResult.getObjectName() + "'. Error count: " + bindingResult.getErrorCount();
    }

    private String getPath(FieldError fieldError) {
        if (!properties.isAddPathToError()) {
            return null;
        }

        String path = null;
        try {
            path = fieldError.unwrap(ConstraintViolationImpl.class)
                             .getPropertyPath()
                             .toString();
        } catch (RuntimeException runtimeException) {
            // only set a path if we have a ConstraintViolation
        }
        return path;
    }
}
