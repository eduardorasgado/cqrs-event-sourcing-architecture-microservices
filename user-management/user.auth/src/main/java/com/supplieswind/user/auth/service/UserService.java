package com.supplieswind.user.auth.service;

import com.supplieswind.user.auth.repository.UserRepository;
import com.supplieswind.user.core.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("customUserService")
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserDetails(repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Incorrect username/password supplied")));
    }

    private UserDetails getUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getAccount().getUsername())
                .password(user.getAccount().getPassword())
                .authorities(user.getAccount().getRoles())

                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)

                .build();
    }
}
