package com.disaster.managementsystem.controller.core;

import com.disaster.managementsystem.param.PageableParam;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

public interface GetAllApi<T> {
    ResponseEntity findAll(Specification<T> specification, PageableParam pageable);
}
