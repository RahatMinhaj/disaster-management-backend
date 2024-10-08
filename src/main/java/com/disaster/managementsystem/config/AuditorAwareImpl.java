package com.disaster.managementsystem.config;

import com.disaster.managementsystem.util.CommonUtils;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
       return Optional.of(CommonUtils.getCurrentUserId());
    }
}
