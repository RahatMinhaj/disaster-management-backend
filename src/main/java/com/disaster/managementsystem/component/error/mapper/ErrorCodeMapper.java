package com.disaster.managementsystem.component.error.mapper;

import com.disaster.managementsystem.component.error.ErrorHandlingProperties;
import com.disaster.managementsystem.component.error.ResponseErrorCode;
import com.disaster.managementsystem.component.error.exception.*;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ErrorCodeMapper {

    private final ErrorHandlingProperties properties;

    public ErrorCodeMapper(ErrorHandlingProperties properties) {
        this.properties = properties;
    }

    public String getErrorCode(Throwable exception) {
        if (exception instanceof FoundException
                || exception instanceof InvalidInputException
                || exception instanceof DataAlreadyExistsException
                || exception instanceof DataNotFoundException
                || exception instanceof MobileNumberVerificationException
                || exception instanceof NotAllowedException
                || exception instanceof DataNotConfiguredException
                || exception instanceof DataNotCreatedException
                || exception instanceof DataShouldBeNotNullException
                || exception instanceof NotPermittedException
                || exception instanceof NotExistsException
                || exception instanceof NotSavedException
                || exception instanceof FailedException
        ) {
            return exception.getMessage();
        }
        else {
            String code = getErrorCodeFromPropertiesOrAnnotation(exception.getClass());
            if (code != null) {
                return code;
            }
            switch (properties.getDefaultErrorCodeStrategy()) {
                case FULL_QUALIFIED_NAME:
                    return exception.getClass().getName();
                case ALL_CAPS:
                    return convertToAllCaps(exception.getClass().getSimpleName());
                default:
                    throw new IllegalArgumentException("Unknown default error code strategy: " + properties.getDefaultErrorCodeStrategy());
            }
        }
    }

    public String getErrorCode(String fieldSpecificErrorCode, String errorCode) {
        if (properties.getCodes().containsKey(fieldSpecificErrorCode)) {
            return properties.getCodes().get(fieldSpecificErrorCode);
        }

        return getErrorCode(errorCode);
    }

    public String getErrorCode(String errorCode) {
        if (properties.getCodes().containsKey(errorCode)) {
            return properties.getCodes().get(errorCode);
        }

        return errorCode;
    }

    private String convertToAllCaps(String exceptionClassName) {
        String result = exceptionClassName.replaceFirst("Exception$", "");
        result = result.replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase(Locale.ENGLISH);
        return result;
    }

    private String getErrorCodeFromPropertiesOrAnnotation(Class<?> exceptionClass) {
        if (exceptionClass == null) {
            return null;
        }
        String exceptionClassName = exceptionClass.getName();
        if (properties.getCodes().containsKey(exceptionClassName)) {
            return properties.getCodes().get(exceptionClassName);
        }
        ResponseErrorCode errorCodeAnnotation = AnnotationUtils.getAnnotation(exceptionClass, ResponseErrorCode.class);
        if (errorCodeAnnotation != null) {
            return errorCodeAnnotation.value();
        }

        if (properties.isSearchSuperClassHierarchy()) {
            return getErrorCodeFromPropertiesOrAnnotation(exceptionClass.getSuperclass());
        } else {
            return null;
        }
    }

}
