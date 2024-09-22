package com.disaster.managementsystem.controller.core;

import com.disaster.managementsystem.support.ApiResponseDto;
import org.springframework.http.ResponseEntity;

public interface CreateApi<S,U> {
    ResponseEntity<ApiResponseDto<S>> save(U param) throws Exception;
}
