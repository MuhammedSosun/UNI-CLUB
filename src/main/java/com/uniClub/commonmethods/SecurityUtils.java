package com.uniClub.commonmethods;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static class Common {
        public static String getUsername() {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }
    }
}
