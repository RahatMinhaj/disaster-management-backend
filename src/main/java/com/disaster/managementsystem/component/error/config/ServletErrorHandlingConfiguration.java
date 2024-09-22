package com.disaster.managementsystem.component.error.config;

import com.disaster.managementsystem.component.error.*;
import com.disaster.managementsystem.component.error.ErrorHandlingProperties;
import com.disaster.managementsystem.component.error.handler.ApiExceptionHandler;
import com.disaster.managementsystem.component.error.handler.FallbackApiExceptionHandler;
import com.disaster.managementsystem.component.error.handler.MissingRequestValueExceptionHandler;
import com.disaster.managementsystem.component.error.mapper.ErrorCodeMapper;
import com.disaster.managementsystem.component.error.mapper.ErrorMessageMapper;
import com.disaster.managementsystem.component.error.mapper.HttpStatusMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(ErrorHandlingProperties.class)
@ConditionalOnProperty(value = "error.handling.enabled", matchIfMissing = true)
@PropertySource("classpath:/error-handling-defaults.properties")
@Import({ValidationErrorHandlingConfiguration.class,
        SpringOrmErrorHandlingConfiguration.class})
public class ServletErrorHandlingConfiguration extends AbstractErrorHandlingConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MissingRequestValueExceptionHandler missingRequestValueExceptionHandler(HttpStatusMapper httpStatusMapper,
                                                                                   ErrorCodeMapper errorCodeMapper,
                                                                                   ErrorMessageMapper errorMessageMapper) {
        return new MissingRequestValueExceptionHandler(httpStatusMapper,
                                                       errorCodeMapper,
                                                       errorMessageMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public ErrorHandlingControllerAdvice errorHandlingControllerAdvice(List<ApiExceptionHandler> handlers,
                                                                       FallbackApiExceptionHandler fallbackApiExceptionHandler,
                                                                       LoggingService loggingService,
                                                                       List<ApiErrorResponseCustomizer> responseCustomizers) {
        return new ErrorHandlingControllerAdvice(handlers,
                                                 fallbackApiExceptionHandler,
                                                 loggingService,
                                                 responseCustomizers);
    }
}
