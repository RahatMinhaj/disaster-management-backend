package com.disaster.managementsystem.component.error.handler;

import com.disaster.managementsystem.support.ApiErrorResponseDto;

public interface FallbackApiExceptionHandler {
    ApiErrorResponseDto handle(Throwable exception);
}
