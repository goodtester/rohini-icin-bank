package com.rohini.icinbank.service;

import com.rohini.icinbank.domain.model.User;
import com.rohini.icinbank.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if(user != null) {
            log.info("User '{}' found", username);
            return user;
        } else {
            log.error("User '{}' not found", username);
            throw new UsernameNotFoundException("Username not found");
        }
    }
    
}
