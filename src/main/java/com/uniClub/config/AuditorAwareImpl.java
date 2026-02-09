package com.uniClub.config;

import com.uniClub.commonmethods.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;
// bu class otomotik bir şekilde kullanıcı adı çekmeyi sağlar
@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityUtils.getUsername());
    }
}
