package com.logicea.cardsapi;

import com.logicea.cardsapi.core.entity.Privilege;
import com.logicea.cardsapi.core.entity.Role;
import com.logicea.cardsapi.core.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * The type Spring security test config.
 */
public class SpringSecurityTestConfig {
    private Authentication authentication;
    /**
     * Sets security context holder.
     */
    public void setSecurityContextHolder() {
        User user = this.setUser();
        UserDetails testUser = new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), user.isEnabled(),
                true, true, true, getAuthorities(user.getRoles()));
        this.authentication = new UsernamePasswordAuthenticationToken(testUser, null, testUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(this.authentication);
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public RequestPostProcessor sessionUser(final Authentication authentication) {
        return request -> {
            final SecurityContext securityContext = new SecurityContextImpl();
            securityContext.setAuthentication(authentication);
            Objects.requireNonNull(request.getSession()).setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
            return request;
        };
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

    private User setUser() {
        User userEntity = new User();
        userEntity.setId(1L);
        userEntity.setEmail("admin@test.com");
        userEntity.setPassword("$2a$10$gZNmQP50w0DVIK7op5yAr.i6Kb/3rntKx/hlrFlyY0he316MDoKnG");
        userEntity.setEnabled(true);
        userEntity.setRoles(this.setRoles());
        return userEntity;
    }

    private List<Role> setRoles() {
        List<Role> roles = new ArrayList<>();
        Role role = new Role("Admin");
        role.setPrivileges(this.setPrivilege());
        return roles;
    }

    private List<Privilege> setPrivilege() {
        List<Privilege> privileges = new ArrayList<>();
        privileges.add(new Privilege("Create"));
        privileges.add(new Privilege("Update"));
        privileges.add(new Privilege("View"));
        privileges.add(new Privilege("Delete"));
        return privileges;
    }
}
