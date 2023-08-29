package com.logicea.cardsapi.core.audit;

import com.logicea.cardsapi.core.entity.User;
import com.logicea.cardsapi.security.models.UserInfo;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditAwareImpl implements AuditorAware<User> {
    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor.
     */
    @Override
    public Optional<User> getCurrentAuditor() {
        UserInfo userDetails = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Optional.of(new User(userDetails.getId(), userDetails.getEmail(), userDetails.isEnabled()));
    }
}