package com.disaster.managementsystem.controller.core;

import com.disaster.managementsystem.support.ApiResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface GetApi<S> {
    ResponseEntity<ApiResponseDto<S>> findById(UUID id);
}
