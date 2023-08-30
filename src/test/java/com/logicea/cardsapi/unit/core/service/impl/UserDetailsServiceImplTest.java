package com.logicea.cardsapi.unit.core.service.impl;

import com.logicea.cardsapi.core.entity.Privilege;
import com.logicea.cardsapi.core.entity.Role;
import com.logicea.cardsapi.core.entity.User;
import com.logicea.cardsapi.core.enums.ErrorCode;
import com.logicea.cardsapi.core.repository.UserRepository;
import com.logicea.cardsapi.core.service.impl.UserDetailsServiceImpl;
import com.logicea.cardsapi.exception.AuthenticationException;
import com.logicea.cardsapi.rest.dto.request.LoginRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        this.userDetailsService = new UserDetailsServiceImpl();
        this.userDetailsService.setUserRepository(this.userRepository);
        this.user = this.setUser();
        this.loginRequest = this.setLoginRequest();
    }

    @AfterEach
    void tearDown() {
        this.user = null;
        this.loginRequest = null;
    }

    @Test
    @DisplayName(value = "Given a valid loginRequest object, should return a userDetails object")
    void testGivenAValidLoginRequestObjectShouldReturnAUserDetailsObject() throws Exception {
        doReturn(Optional.of(this.user)).when(this.userRepository).findUserByEmail(this.loginRequest.getEmailAddress());
        final UserDetails userDetails = assertDoesNotThrow(() ->
                this.userDetailsService.loadUserByUsername(this.loginRequest.getEmailAddress()));
        verify(this.userRepository, times(1)).findUserByEmail(anyString());
        assertNotNull(userDetails);
        assertEquals(this.loginRequest.getEmailAddress(), userDetails.getUsername());
    }

    @Test
    @DisplayName(value = "Given an invalid username, should throw AuthenticationException")
    void testGivenAnInvalidUsernameShouldThrowAuthenticationException() {
        this.loginRequest.setEmailAddress("admin1.test@test.com");
        String expectedMessage = String.format(ErrorCode.ERROR_1000.getMessage(), this.loginRequest.getEmailAddress());

        final AuthenticationException thrown = assertThrows(AuthenticationException.class, () ->
                this.userDetailsService.loadUserByUsername(this.loginRequest.getEmailAddress()));
        verify(this.userRepository, times(1)).findUserByEmail(anyString());
        assertNotNull(thrown);
        assertEquals(expectedMessage, thrown.getMessage());
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

    private Set<Role> setRoles() {
        Set<Role> roles = new HashSet<>();
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

    private LoginRequest setLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setEmailAddress("admin@test.com");
        request.setPassword("password");
        return request;
    }
}