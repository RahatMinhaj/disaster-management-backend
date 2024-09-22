package com.disaster.managementsystem.component.error;

import com.disaster.managementsystem.support.ApiErrorResponseDto;

public interface ApiErrorResponseCustomizer {
    void customize(ApiErrorResponseDto response);
}
