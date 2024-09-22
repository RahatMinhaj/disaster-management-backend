package com.disaster.managementsystem.component.error.mapper;


import com.disaster.managementsystem.component.error.ErrorHandlingProperties;
import org.springframework.stereotype.Component;

import static org.springframework.util.StringUtils.hasText;

@Component
public class ErrorMessageMapper {
    private final ErrorHandlingProperties properties;

    public ErrorMessageMapper(ErrorHandlingProperties properties) {
        this.properties = properties;
    }

    public String getErrorMessage(Throwable exception) {
        String code = getErrorMessageFromProperties(exception.getClass());
        if (hasText(code)) {
            return code;
        }
        return exception.getMessage();
    }

    public String getErrorMessage(String fieldSpecificCode, String code, String defaultMessage) {
        if (properties.getMessages().containsKey(fieldSpecificCode)) {
            return properties.getMessages().get(fieldSpecificCode);
        }

        return getErrorMessage(code, defaultMessage);
    }

    public String getErrorMessage(String code, String defaultMessage) {
        if (properties.getMessages().containsKey(code)) {
            return properties.getMessages().get(code);
        }

        return defaultMessage;
    }

    private String getErrorMessageFromProperties(Class<?> exceptionClass) {
        if (exceptionClass == null) {
            return null;
        }
        String exceptionClassName = exceptionClass.getName();
        if (properties.getMessages().containsKey(exceptionClassName)) {
            return properties.getMessages().get(exceptionClassName);
        }
        if (properties.isSearchSuperClassHierarchy()) {
            return getErrorMessageFromProperties(exceptionClass.getSuperclass());
        } else {
            return null;
        }
    }
}
