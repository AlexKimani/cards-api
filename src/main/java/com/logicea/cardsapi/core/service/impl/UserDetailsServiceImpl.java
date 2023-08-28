package com.logicea.cardsapi.core.service.impl;

import com.logicea.cardsapi.core.entity.Privilege;
import com.logicea.cardsapi.core.entity.Role;
import com.logicea.cardsapi.core.entity.User;
import com.logicea.cardsapi.core.enums.ErrorCode;
import com.logicea.cardsapi.core.repository.UserRepository;
import com.logicea.cardsapi.core.service.UserDetailsService;
import com.logicea.cardsapi.exception.AuthenticationException;
import com.logicea.cardsapi.rest.dto.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    /**
     * Load user by username.
     *
     * @param loginRequest the user login details
     * @return the user details
     */
    @Override
    public UserDetails loadUserByUserDetails(LoginRequest loginRequest) {
        try {
            UserDetails userDetails = this.loadUserByUserName(loginRequest.getEmailAddress());
            if (!this.passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
                log.warn(String.format(ErrorCode.ERROR_1001.getMessage(), loginRequest.getEmailAddress()));
                throw new AuthenticationException(
                        String.format(ErrorCode.ERROR_1001.getMessage(), loginRequest.getEmailAddress()));
            }
            return userDetails;
        } catch (final Exception ex) {
            log.error("Error: {} ", ex.getMessage(), ex);
            throw new AuthenticationException(ex.getMessage());
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return this.getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(final Collection<Role> roles) {
        final List<String> privileges = new ArrayList<>();
        final List<Privilege> collection = new ArrayList<>();
        roles.forEach(role -> {
            privileges.add(role.getRoleName());
            collection.addAll(role.getPrivileges());
        });
        collection.stream().map(Privilege::getPrivilegeName).forEach(privileges::add);
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
        return privileges.stream().map(SimpleGrantedAuthority::new).collect(toList());
    }

    /**
     * Load user by username.
     *
     * @param userEmail the user email
     * @return the user details
     */
    @Override
    public UserDetails loadUserByUserName(String userEmail) {
        final User user = this.userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> {
                    log.warn(String.format(ErrorCode.ERROR_1000.getMessage(), userEmail));
                    return new AuthenticationException(String.format(ErrorCode.ERROR_1000.getMessage(), userEmail));
                });
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.isEnabled(),
                true, true, true, getAuthorities(user.getRoles()));
    }
}
