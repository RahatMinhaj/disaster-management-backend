package com.disaster.managementsystem.repository;

import com.disaster.managementsystem.entity.Role;
import com.disaster.managementsystem.repository.core.CustomRepository;

import java.util.UUID;

public interface RoleRepository extends CustomRepository<Role, UUID> {
}