package com.disaster.managementsystem.component.error.handler;

import com.disaster.managementsystem.component.error.ResponseErrorProperty;
import com.disaster.managementsystem.component.error.mapper.ErrorCodeMapper;
import com.disaster.managementsystem.component.error.mapper.ErrorMessageMapper;
import com.disaster.managementsystem.component.error.mapper.HttpStatusMapper;
import com.disaster.managementsystem.support.ApiErrorResponseDto;
import com.disaster.managementsystem.support.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class DefaultFallbackApiExceptionHandler implements FallbackApiExceptionHandler {
    private final HttpStatusMapper httpStatusMapper;
    private final ErrorCodeMapper errorCodeMapper;
    private final ErrorMessageMapper errorMessageMapper;

    public DefaultFallbackApiExceptionHandler(HttpStatusMapper httpStatusMapper,
                                              ErrorCodeMapper errorCodeMapper,
                                              ErrorMessageMapper errorMessageMapper) {
        this.httpStatusMapper = httpStatusMapper;
        this.errorCodeMapper = errorCodeMapper;
        this.errorMessageMapper = errorMessageMapper;
    }

    @Override
    public ApiErrorResponseDto handle(Throwable exception) {
        HttpStatusCode statusCode = httpStatusMapper.getHttpStatus(exception);
        String errorCode = errorCodeMapper.getErrorCode(exception);
        String errorMessage = errorMessageMapper.getErrorMessage(exception);
        //String errorMessage = apiService.getMessage(errorCode);

        ApiErrorResponseDto response = ApiErrorResponseDto.builder()
                .status(statusCode.value())
                .message(errorMessage)
                .error(ErrorDto.builder().code(errorCode).build())
                .build();


        response.getError().getProperties().putAll(getMethodResponseErrorProperties(exception));
        response.getError().getProperties().putAll(getFieldResponseErrorProperties(exception));

        return response;
    }

    private Map<String, Object> getFieldResponseErrorProperties(Throwable exception) {
        Map<String, Object> result = new HashMap<>();
        ReflectionUtils.doWithFields(exception.getClass(), field -> {
            if (field.isAnnotationPresent(ResponseErrorProperty.class)) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(exception);
                    if (value != null || field.getAnnotation(ResponseErrorProperty.class).includeIfNull()) {
                        result.put(getPropertyName(field), value);
                    }
                } catch (IllegalAccessException e) {
                    log.error(String.format("Unable to use field result of field %s.%s", exception.getClass().getName(), field.getName()));
                }
            }
        });
        return result;
    }

    private Map<String, Object> getMethodResponseErrorProperties(Throwable exception) {
        Map<String, Object> result = new HashMap<>();
        Class<? extends Throwable> exceptionClass = exception.getClass();
        ReflectionUtils.doWithMethods(exceptionClass, method -> {
            if (method.isAnnotationPresent(ResponseErrorProperty.class)
                    && method.getReturnType() != Void.TYPE
                    && method.getParameterCount() == 0) {
                try {
                    method.setAccessible(true);

                    Object value = method.invoke(exception);
                    if (value != null || method.getAnnotation(ResponseErrorProperty.class).includeIfNull()) {
                        result.put(getPropertyName(exceptionClass, method),
                                   value);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error(String.format("Unable to use method result of method %s.%s", exceptionClass.getName(), method.getName()));
                }
            }
        });
        return result;
    }

    private String getPropertyName(Field field) {
        ResponseErrorProperty annotation = AnnotationUtils.getAnnotation(field, ResponseErrorProperty.class);
        assert annotation != null;
        if (StringUtils.hasText(annotation.value())) {
            return annotation.value();
        }

        return field.getName();
    }

    private String getPropertyName(Class<? extends Throwable> exceptionClass, Method method) {
        ResponseErrorProperty annotation = AnnotationUtils.getAnnotation(method, ResponseErrorProperty.class);
        assert annotation != null;
        if (StringUtils.hasText(annotation.value())) {
            return annotation.value();
        }

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(exceptionClass);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if (propertyDescriptor.getReadMethod().equals(method)) {
                    return propertyDescriptor.getName();
                }
            }
        } catch (IntrospectionException e) {
            //ignore
        }

        return method.getName();
    }
}
