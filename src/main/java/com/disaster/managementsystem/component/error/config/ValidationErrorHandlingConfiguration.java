package com.disaster.managementsystem.component.error.config;

import com.disaster.managementsystem.component.error.ErrorHandlingProperties;
import com.disaster.managementsystem.component.error.handler.ConstraintViolationApiExceptionHandler;
import com.disaster.managementsystem.component.error.mapper.ErrorCodeMapper;
import com.disaster.managementsystem.component.error.mapper.ErrorMessageMapper;
import com.disaster.managementsystem.component.error.mapper.HttpStatusMapper;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ConstraintViolationException.class)
public class ValidationErrorHandlingConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ConstraintViolationApiExceptionHandler constraintViolationApiExceptionHandler(ErrorHandlingProperties properties,
                                                                                         HttpStatusMapper httpStatusMapper,
                                                                                         ErrorCodeMapper errorCodeMapper,
                                                                                         ErrorMessageMapper errorMessageMapper) {
        return new ConstraintViolationApiExceptionHandler(properties, httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

}
