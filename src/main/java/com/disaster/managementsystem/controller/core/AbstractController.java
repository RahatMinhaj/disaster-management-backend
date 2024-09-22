package com.disaster.managementsystem.controller.core;

import com.disaster.managementsystem.dto.core.BaseDto;
import com.disaster.managementsystem.support.*;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractController {
    public static final Logger log = getLogger(lookup().lookupClass());

    public  <S extends BaseDto> ResponseEntity<ApiResponseDto<S>> generateResponse(S data, HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(
                        ApiResponseDto.<S>builder()
                                .nonce(Instant.now().toEpochMilli())
                                .status(status.value())
                                .message(message)
                                .payload(data)
                                .build()
                );
    }

    public  <S> ResponseEntity<ApiResponseDto<S>> generateResponse(S data, HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(
                        ApiResponseDto.<S>builder()
                                .nonce(Instant.now().toEpochMilli())
                                .status(status.value())
                                .message(message)
                                .payload(data)
                                .build()
                );
    }

    public  <S> ResponseEntity<ApiResponseDto<S>> generateResponse(Optional<S> data, HttpStatus status, String message) {
        if (data.isPresent()) {
            return ResponseEntity
                    .status(status)
                    .body(
                            ApiResponseDto.<S>builder()
                                    .nonce(Instant.now().toEpochMilli())
                                    .status(status.value())
                                    .message(message)
                                    .payload(data.get())
                                    .build()
                    );
        } else {
            ErrorDto errorDto = ErrorDto.builder()
                    .code("NOT_FOUND")
                    .message("Entity is not found")
                    .build();
            return ResponseEntity
                    .status(206)
                    .body(
                            ApiResponseDto.<S>builder()
                                    .nonce(Instant.now().toEpochMilli())
                                    .status(HttpStatus.NOT_FOUND.value())
                                    .message("Entity is not found")
                                    .error(errorDto)
                                    .build()
                    );
        }
    }

    public  <S> ResponseEntity<ListResponseDto<S>> generateResponse(List<S> data, HttpStatus status, String message) {
        int total = data == null ? 0 : data.size();
        return ResponseEntity
                .status(status)
                .body(
                        ListResponseDto.<S>builder()
                                .nonce(Instant.now().toEpochMilli())
                                .status(status.value())
                                .message(message)
                                .total(total)
                                .payload(data)
                                .build()
                );

    }

    public  <S> ResponseEntity<PaginationResponseDto<S>> generateResponse(Page<S> page, HttpStatus status, String message) {
        return
                ResponseEntity
                        .status(status)
                        .body(
                                PaginationResponseDto.<S>builder()
                                        .nonce(Instant.now().toEpochMilli())
                                        .status(status.value())
                                        .message(message)
                                        .payload(page.getContent())
                                        .pageable(PageDto.builder()
                                                .pageNumber(page.getNumber()+1)
                                                .pageSize(page.getSize())
                                                .totalElements(page.getTotalElements())
                                                .totalPages(page.getTotalPages())
                                                .first(page.isFirst())
                                                .last(page.isLast())
                                                .build())
                                        .build()
                        );

    }

    public  ResponseEntity<DeleteResponseDto> generateResponse(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(
                        DeleteResponseDto.builder()
                                .nonce(Instant.now().toEpochMilli())
                                .status(status.value())
                                .message(message)
                                .deleted(true)
                                .build()
                );
    }
}
