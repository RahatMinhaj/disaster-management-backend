package com.disaster.managementsystem.component.error.handler;

import com.disaster.managementsystem.support.ApiErrorResponseDto;

public interface ApiExceptionHandler {
    boolean canHandle(Throwable exception);

    ApiErrorResponseDto handle(Throwable exception);
}
