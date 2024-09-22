package com.disaster.managementsystem.util;


import com.disaster.managementsystem.entity.User;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

@UtilityClass
@Slf4j
public class CommonUtils {
    public Optional<User> getLoggedInUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || !authentication.isAuthenticated() || authentication.getName().equals("anonymousUser")) {
            return Optional.empty();
        }
        return Optional.ofNullable((User) authentication.getPrincipal());
    }
    public String getCurrentUserId() {
        User loggedInUserDetails = CommonUtils.getLoggedInUserDetails().orElse(null);
        return Objects.nonNull(loggedInUserDetails) ? String.valueOf(loggedInUserDetails.getId()) : "Anonymous User";
    }
}
