package com.team_one.expressoh.controllers;

import com.team_one.expressoh.dto.AuthRequest;
import com.team_one.expressoh.model.Profile;
import com.team_one.expressoh.model.Users;
import com.team_one.expressoh.repository.UsersRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// Customer Profile Endpoint
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/profile")
    public ResponseEntity<Users> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));
        // Omit sensitive information before returning
        user.setPassword(null);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/profile")
    public ResponseEntity<Users> saveProfile(@Valid @RequestBody AuthRequest authRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Profile newProfile = new Profile();
        newProfile.setFirstName(authRequest.getFirstName());
        newProfile.setLastName(authRequest.getLastName());
        newProfile.setUsersId(user.getId());
        user.setProfile(newProfile);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}