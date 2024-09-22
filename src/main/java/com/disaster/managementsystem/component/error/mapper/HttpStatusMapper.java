package com.disaster.managementsystem.component.error.mapper;

import com.disaster.managementsystem.component.error.ErrorHandlingProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@Component
public class HttpStatusMapper {
    private final ErrorHandlingProperties properties;

    public HttpStatusMapper(ErrorHandlingProperties properties) {
        this.properties = properties;
    }

    public HttpStatusCode getHttpStatus(Throwable exception) {
        return getHttpStatus(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public HttpStatusCode getHttpStatus(Throwable exception, HttpStatus defaultHttpStatus) {
        HttpStatusCode status = getHttpStatusFromPropertiesOrAnnotation(exception.getClass());
        if (status != null) {
            return status;
        }

        if (exception instanceof ResponseStatusException) {
            return ((ResponseStatusException) exception).getStatusCode();
        }

        return defaultHttpStatus;
    }

    private HttpStatusCode getHttpStatusFromPropertiesOrAnnotation(Class<?> exceptionClass) {
        if (exceptionClass == null) {
            return null;
        }
        String exceptionClassName = exceptionClass.getName();
        if (properties.getHttpStatuses().containsKey(exceptionClassName)) {
            return properties.getHttpStatuses().get(exceptionClassName);
        }

        ResponseStatus responseStatus = AnnotationUtils.getAnnotation(exceptionClass, ResponseStatus.class);
        if (responseStatus != null) {
            return responseStatus.value();
        }

        if (properties.isSearchSuperClassHierarchy()) {
            return getHttpStatusFromPropertiesOrAnnotation(exceptionClass.getSuperclass());
        } else {
            return null;
        }
    }

}
