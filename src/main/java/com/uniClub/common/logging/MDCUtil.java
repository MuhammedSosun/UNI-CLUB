package com.uniClub.common.logging;

import org.slf4j.MDC;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class MDCUtil {

    private static final String TRACE_ID = "trace_id";
    private static final String USERNAME = "username";
    private static final String ROLE = "role";

    private MDCUtil() {

    }

    public static void setUseContext() {
        if (MDC.get(TRACE_ID) == null) {
            MDC.put(TRACE_ID, UUID.randomUUID().toString());
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication  != null && authentication .isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            MDC.put(USERNAME, authentication.getName());
            MDC.put(ROLE, authentication.getAuthorities().toString());
        }else {
            MDC.put("username", "anonymous");
            MDC.put("role", "NONE");
        }
    }
    public static void clear(){
        MDC.clear();
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }
    public static String getUsername() {
        return MDC.get(USERNAME);
    }
    public static String getRole() {
        return MDC.get(ROLE);
    }
}
