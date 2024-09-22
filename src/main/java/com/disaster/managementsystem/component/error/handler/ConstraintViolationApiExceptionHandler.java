package com.disaster.managementsystem.component.error.handler;

import com.disaster.managementsystem.component.error.*;
import com.disaster.managementsystem.component.error.ErrorHandlingProperties;
import com.disaster.managementsystem.component.error.mapper.ErrorCodeMapper;
import com.disaster.managementsystem.component.error.mapper.ErrorMessageMapper;
import com.disaster.managementsystem.component.error.mapper.HttpStatusMapper;
import com.disaster.managementsystem.support.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class ConstraintViolationApiExceptionHandler extends AbstractApiExceptionHandler {
    private final ErrorHandlingProperties properties;

    public ConstraintViolationApiExceptionHandler(ErrorHandlingProperties properties,
                                                  HttpStatusMapper httpStatusMapper,
                                                  ErrorCodeMapper errorCodeMapper,
                                                  ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
        this.properties = properties;
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof ConstraintViolationException;
    }

    @Override
    public ApiErrorResponseDto handle(Throwable exception) {
        ConstraintViolationException ex = (ConstraintViolationException) exception;

        ApiErrorResponseDto response =
                ApiErrorResponseDto.builder()
                        .status(getHttpStatus(exception, HttpStatus.BAD_REQUEST).value())
                        .message(ex.getMessage())
                        .error(ErrorDto.builder().code(getErrorCode(exception)).build())
                        .build();

        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        violations.stream()
                // sort violations to ensure deterministic order
                .sorted(Comparator.comparing(constraintViolation -> constraintViolation.getPropertyPath().toString()))
                .map(constraintViolation -> {
                    Optional<Path.Node> leafNode = getLeafNode(constraintViolation.getPropertyPath());
                    if (leafNode.isPresent()) {
                        Path.Node node = leafNode.get();
                        ElementKind elementKind = node.getKind();
                        if (elementKind == ElementKind.PROPERTY) {
                            return new ApiFieldError(getCode(constraintViolation),
                                    node.toString(),
                                    getMessage(constraintViolation),
                                    constraintViolation.getInvalidValue(),
                                    getPath(constraintViolation));
                        } else if (elementKind == ElementKind.BEAN) {
                            return new ApiGlobalError(getCode(constraintViolation),
                                    getMessage(constraintViolation));
                        } else if (elementKind == ElementKind.PARAMETER) {
                            return new ApiParameterError(getCode(constraintViolation),
                                    node.toString(),
                                    getMessage(constraintViolation),
                                    constraintViolation.getInvalidValue());
                        } else {
                            log.warn("Unable to convert constraint violation with element kind {}: {}", elementKind, constraintViolation);
                            return null;
                        }
                    } else {
                        log.warn("Unable to convert constraint violation: {}", constraintViolation);
                        return null;
                    }
                })
                .forEach(error -> {
                    if (error instanceof ApiFieldError) {
                        response.getError().getFieldErrors().add((ApiFieldError) error);
                    } else if (error instanceof ApiGlobalError) {
                        response.getError().getGlobalErrors().add((ApiGlobalError) error);
                    } else if (error instanceof ApiParameterError) {
                        response.getError().getParameterErrors().add((ApiParameterError) error);
                    }
                });

        return response;
    }

    private Optional<Path.Node> getLeafNode(Path path) {
        return StreamSupport.stream(path.spliterator(), false).reduce((a, b) -> b);
    }

    private String getPath(ConstraintViolation<?> constraintViolation) {
        if (!properties.isAddPathToError()) {
            return null;
        }

        return getPathWithoutPrefix(constraintViolation.getPropertyPath());
    }
    private String getPathWithoutPrefix(Path path) {
        String collect = StreamSupport.stream(path.spliterator(), false)
                .limit(2)
                .map(Path.Node::getName)
                .collect(Collectors.joining("."));
        String substring = path.toString().substring(collect.length());
        return substring.startsWith(".") ? substring.substring(1) : substring;
    }

    private String getCode(ConstraintViolation<?> constraintViolation) {
        String code = constraintViolation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
        String fieldSpecificCode = constraintViolation.getPropertyPath().toString() + "." + code;
        return errorCodeMapper.getErrorCode(fieldSpecificCode, code);
    }

    private String getMessage(ConstraintViolation<?> constraintViolation) {
        String code = constraintViolation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
        String fieldSpecificCode = constraintViolation.getPropertyPath().toString() + "." + code;
        return errorMessageMapper.getErrorMessage(fieldSpecificCode, code, constraintViolation.getMessage());
    }

    private String getMessage(ConstraintViolationException exception) {
        return "Validation failed. Error count: " + exception.getConstraintViolations().size();
    }
}
