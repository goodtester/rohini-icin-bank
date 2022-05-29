package com.rohini.icinbank.service;

import com.rohini.icinbank.domain.common.TokenResponse;
import com.rohini.icinbank.domain.common.UsernamePassword;
import com.rohini.icinbank.domain.model.User;
import com.rohini.icinbank.repository.UserRepository;
import com.rohini.icinbank.security.JwtTokenProvider;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public UsernamePassword register(UsernamePassword req) {
        try {
            if(userRepository.findByUsername(req.getUsername()) != null) 
                throw new Exception("User " + req.getUsername() + " is already exist");
            
            User user = new User();
            user.setUsername(req.getUsername());
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            userRepository.save(user);

            req.setPassword("*".repeat(req.getPassword().length()));
            log.info("User {} saved", req.getUsername());
            return req;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public TokenResponse generateToken(UsernamePassword req) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenProvider.generateToken(authentication);

            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(jwt);
            log.info("Token created");
            return tokenResponse;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
