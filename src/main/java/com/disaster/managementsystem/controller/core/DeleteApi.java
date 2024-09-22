package com.disaster.managementsystem.controller.core;

import com.disaster.managementsystem.support.DeleteResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface DeleteApi {
    ResponseEntity<DeleteResponseDto> deleteById(UUID id) throws Exception;
}
