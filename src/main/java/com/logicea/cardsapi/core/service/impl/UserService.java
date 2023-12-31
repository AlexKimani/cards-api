package com.logicea.cardsapi.core.service.impl;

import com.logicea.cardsapi.core.entity.User;
import com.logicea.cardsapi.core.enums.ErrorCode;
import com.logicea.cardsapi.core.repository.UserRepository;
import com.logicea.cardsapi.exception.AuthenticationException;
import com.logicea.cardsapi.security.models.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserDetails(username, this.userRepository, log);
    }

    static UserDetails getUserDetails(String username, UserRepository userRepository, Logger log) {
        final User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> {
                    log.warn(String.format(ErrorCode.ERROR_1000.getMessage(), username));
                    return new AuthenticationException(String.format(ErrorCode.ERROR_1000.getMessage(), username));
                });
        return new UserInfo(user);
    }
}
