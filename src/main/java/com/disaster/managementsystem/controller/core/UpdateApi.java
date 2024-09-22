package com.disaster.managementsystem.controller.core;

import com.disaster.managementsystem.support.ApiResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface UpdateApi<S,U> {
    ResponseEntity<ApiResponseDto<S>> update(UUID id, U param) throws Exception;
}
