package com.disaster.managementsystem.repository;

import com.disaster.managementsystem.entity.User;
import com.disaster.managementsystem.repository.core.CustomRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CustomRepository<User, UUID> {
    Optional<User> findByUserNameIgnoreCase(String userName);
    boolean existsByUserNameIgnoreCaseOrEmailIgnoreCase(String userName, String email);
    boolean existsByUserNameIgnoreCase(String userName);
}