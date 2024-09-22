package com.disaster.managementsystem.component.error.config;

import com.disaster.managementsystem.component.error.ErrorHandlingProperties;
import com.disaster.managementsystem.component.error.handler.ObjectOptimisticLockingFailureApiExceptionHandler;
import com.disaster.managementsystem.component.error.mapper.ErrorCodeMapper;
import com.disaster.managementsystem.component.error.mapper.ErrorMessageMapper;
import com.disaster.managementsystem.component.error.mapper.HttpStatusMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@Configuration
@ConditionalOnClass(ObjectOptimisticLockingFailureException.class)
public class SpringOrmErrorHandlingConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ObjectOptimisticLockingFailureApiExceptionHandler objectOptimisticLockingFailureApiExceptionHandler(ErrorHandlingProperties properties,
                                                                                                               HttpStatusMapper httpStatusMapper,
                                                                                                               ErrorCodeMapper errorCodeMapper,
                                                                                                               ErrorMessageMapper errorMessageMapper) {
        return new ObjectOptimisticLockingFailureApiExceptionHandler(properties, httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

}
