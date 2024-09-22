package com.disaster.managementsystem.repository;

import com.disaster.managementsystem.entity.Crisis;
import com.disaster.managementsystem.repository.core.CustomRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CrisisRepository extends CustomRepository<Crisis, UUID> {
}