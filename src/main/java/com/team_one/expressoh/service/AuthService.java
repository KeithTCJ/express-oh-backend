package com.team_one.expressoh.service;

import com.team_one.expressoh.dto.AuthRequest;
import com.team_one.expressoh.dto.AuthResponse;
import com.team_one.expressoh.dto.LoginRequest;
import com.team_one.expressoh.model.Users;
import com.team_one.expressoh.repository.UsersRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;



    public AuthResponse signUp(AuthRequest signUpRequest) throws Exception {
        if (usersRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new Exception("Email already in use.");
        }

        // Create and set up the user
        Users user = new Users();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(signUpRequest.getRole());

        // Save the user (the cascade setting will save the profile too)
        usersRepository.save(user);

        // If you need a UserDetails instance, you could do:
        // String token = jwtUtils.generateToken(securityUser);

        // 7. Build the authentication response, including only the desired fields.
        AuthResponse response = new AuthResponse();
        response.setMessage("User registered successfully.");
        response.setEmail(user.getEmail());

        // 8. Return the response object.
        return response;
    }

    public AuthResponse signIn(@Valid LoginRequest signInRequest) {
        // Authenticate (throws exception if authentication fails)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getEmail(),
                        signInRequest.getPassword()
                )
        );

        // Fetch user along with Profile details
        Users user = usersRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Build security user for token generation (could be a simplified conversion)
        org.springframework.security.core.userdetails.User securityUser =
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
                );

        String jwt = jwtUtils.generateToken(securityUser);
        String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), securityUser);

        AuthResponse response = new AuthResponse();
        response.setToken(jwt);
        response.setRefreshToken(refreshToken);
        response.setMessage("User signed in successfully.");
        response.setEmail(user.getEmail());

        // Use the user profile -->
        if (user.getProfile() != null) {
            response.setFirstName(user.getProfile().getFirstName());
            response.setLastName(user.getProfile().getLastName());
            response.setPhone(user.getProfile().getPhone());
            response.setAddress(user.getProfile().getAddress());
            // Add additional fields as needed
        }

        return response;
    }
}