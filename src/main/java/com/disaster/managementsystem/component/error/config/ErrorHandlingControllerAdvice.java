package com.disaster.managementsystem.component.error.config;

import com.disaster.managementsystem.component.error.ApiErrorResponseCustomizer;
import com.disaster.managementsystem.component.error.LoggingService;
import com.disaster.managementsystem.component.error.handler.ApiExceptionHandler;
import com.disaster.managementsystem.component.error.handler.FallbackApiExceptionHandler;
import com.disaster.managementsystem.support.ApiErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Locale;

@Slf4j
@RestControllerAdvice()
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ErrorHandlingControllerAdvice {
    private final List<ApiExceptionHandler> handlers;
    private final FallbackApiExceptionHandler fallbackHandler;
    private final LoggingService loggingService;
    private final List<ApiErrorResponseCustomizer> responseCustomizers;

    public ErrorHandlingControllerAdvice(List<ApiExceptionHandler> handlers,
                                         FallbackApiExceptionHandler fallbackHandler,
                                         LoggingService loggingService,
                                         List<ApiErrorResponseCustomizer> responseCustomizers) {
        this.handlers = handlers;
        this.fallbackHandler = fallbackHandler;
        this.loggingService = loggingService;
        this.responseCustomizers = responseCustomizers;
        this.handlers.sort(AnnotationAwareOrderComparator.INSTANCE);

        log.info("Error Handling Spring Boot Starter active with {} handlers", this.handlers.size());
        log.debug("Handlers: {}", this.handlers);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(Throwable exception, WebRequest webRequest, Locale locale) {
        log.debug("webRequest: {}", webRequest);
        log.debug("locale: {}", locale);
        log.error("exception: {}", exception.getMessage());

        ApiErrorResponseDto errorResponse = null;
        for (ApiExceptionHandler handler : handlers) {
            if (handler.canHandle(exception)) {
                errorResponse = handler.handle(exception);
                break;
            }
        }

        if (errorResponse == null) {
            errorResponse = fallbackHandler.handle(exception);
        }

        for (ApiErrorResponseCustomizer responseCustomizer : responseCustomizers) {
            responseCustomizer.customize(errorResponse);
        }

        loggingService.logException(errorResponse, exception);
        return ResponseEntity.status(errorResponse.getStatus())
                             .body(errorResponse);
    }
}
