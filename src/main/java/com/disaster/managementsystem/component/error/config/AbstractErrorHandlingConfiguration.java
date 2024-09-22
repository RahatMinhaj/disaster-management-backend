package com.disaster.managementsystem.component.error.config;

import com.disaster.managementsystem.component.error.ApiErrorResponseSerializer;
import com.disaster.managementsystem.component.error.ErrorHandlingProperties;
import com.disaster.managementsystem.component.error.LoggingService;
import com.disaster.managementsystem.component.error.handler.*;
import com.disaster.managementsystem.component.error.mapper.ErrorCodeMapper;
import com.disaster.managementsystem.component.error.mapper.ErrorMessageMapper;
import com.disaster.managementsystem.component.error.mapper.HttpStatusMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public abstract class AbstractErrorHandlingConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public LoggingService loggingService(ErrorHandlingProperties properties) {
        return new LoggingService(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpStatusMapper httpStatusMapper(ErrorHandlingProperties properties) {
        return new HttpStatusMapper(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public ErrorCodeMapper errorCodeMapper(ErrorHandlingProperties properties) {
        return new ErrorCodeMapper(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public ErrorMessageMapper errorMessageMapper(ErrorHandlingProperties properties) {
        return new ErrorMessageMapper(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public FallbackApiExceptionHandler defaultHandler(HttpStatusMapper httpStatusMapper,
                                                      ErrorCodeMapper errorCodeMapper,
                                                      ErrorMessageMapper errorMessageMapper) {
        return new DefaultFallbackApiExceptionHandler(httpStatusMapper,
                                                      errorCodeMapper,
                                                      errorMessageMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public TypeMismatchApiExceptionHandler typeMismatchApiExceptionHandler(ErrorHandlingProperties properties,
                                                                           HttpStatusMapper httpStatusMapper,
                                                                           ErrorCodeMapper errorCodeMapper,
                                                                           ErrorMessageMapper errorMessageMapper) {
        return new TypeMismatchApiExceptionHandler(properties, httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageNotReadableApiExceptionHandler httpMessageNotReadableApiExceptionHandler(ErrorHandlingProperties properties,
                                                                                               HttpStatusMapper httpStatusMapper,
                                                                                               ErrorCodeMapper errorCodeMapper,
                                                                                               ErrorMessageMapper errorMessageMapper) {
        return new HttpMessageNotReadableApiExceptionHandler(properties, httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public BindApiExceptionHandler bindApiExceptionHandler(ErrorHandlingProperties properties,
                                                           HttpStatusMapper httpStatusMapper,
                                                           ErrorCodeMapper errorCodeMapper,
                                                           ErrorMessageMapper errorMessageMapper) {
        return new BindApiExceptionHandler(properties, httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiErrorResponseSerializer apiErrorResponseSerializer(ErrorHandlingProperties properties) {
        return new ApiErrorResponseSerializer(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public FoundExceptionHandler foundExceptionHandler(HttpStatusMapper httpStatusMapper,
                                                       ErrorCodeMapper errorCodeMapper,
                                                       ErrorMessageMapper errorMessageMapper) {
        return new FoundExceptionHandler(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }
}
