package com.disaster.managementsystem.repository;

import com.disaster.managementsystem.entity.ProductCategory;
import com.disaster.managementsystem.repository.core.CustomRepository;

import java.util.UUID;

public interface ProductCategoryRepository extends CustomRepository<ProductCategory, UUID> {
}
