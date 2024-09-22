package com.disaster.managementsystem.repository;

import com.disaster.managementsystem.entity.District;
import com.disaster.managementsystem.repository.core.CustomRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DistrictRepository extends CustomRepository<District, UUID> {
}