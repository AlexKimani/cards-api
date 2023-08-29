package com.logicea.cardsapi.core.service.impl;

import com.logicea.cardsapi.core.entity.Privilege;
import com.logicea.cardsapi.core.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class AuthoritiesService {
    public Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return this.getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(final Collection<Role> roles) {
        final List<String> privileges = new ArrayList<>();
        final List<Privilege> collection = new ArrayList<>();
        roles.forEach(role -> {
            privileges.add(role.getRoleName().toUpperCase());
            collection.addAll(role.getPrivileges());
        });
        collection.stream().map(privilege -> privilege.getPrivilegeName().toUpperCase()).forEach(privileges::add);
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
        return privileges.stream().map(SimpleGrantedAuthority::new).collect(toList());
    }
}
