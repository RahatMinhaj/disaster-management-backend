package com.disaster.managementsystem.controller.core;

import com.disaster.managementsystem.support.ApiResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface StatusUpdateApi<S> {
    ResponseEntity<ApiResponseDto<S>> updateStatus(UUID id) throws Exception;
}
