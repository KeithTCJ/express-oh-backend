package com.team_one.expressoh.controllers;

import com.team_one.expressoh.dto.ProfileRequest;
import com.team_one.expressoh.model.Profile;
import com.team_one.expressoh.model.Users;
import com.team_one.expressoh.service.UsersService;
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
    private UsersService usersService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileRequest> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Users user = usersService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No user found."));

        ProfileRequest returnProfile = new ProfileRequest();
        returnProfile.setEmail(user.getEmail());
        returnProfile.setFirstName(user.getProfile().getFirstName());
        returnProfile.setLastName(user.getProfile().getLastName());
        returnProfile.setPhone(user.getProfile().getPhone());
        returnProfile.setAddress(user.getProfile().getAddress());
        returnProfile.setLastModifiedDateTime(user.getProfile().getLastModifiedDateTime());

        return new ResponseEntity<>(returnProfile, HttpStatus.OK);
    }

    @PostMapping("/profile")
    public ResponseEntity<ProfileRequest> saveProfile(@Valid @RequestBody ProfileRequest profileRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Users user = usersService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found."));

        Profile profileToUpdate = user.getProfile();

        if (profileToUpdate == null) {                                  // check if a profile already exists for this user
            profileToUpdate = new Profile();                            // If no profile exists, create a new one and link it
            user.setProfile(profileToUpdate);                           // This links the new profile to the user
        }

        profileToUpdate.setFirstName(profileRequest.getFirstName());    // update the fields of the existing (or newly created) profile
        profileToUpdate.setLastName(profileRequest.getLastName());
        profileToUpdate.setPhone(profileRequest.getPhone());
        profileToUpdate.setAddress(profileRequest.getAddress());
        profileToUpdate.setCardName(profileRequest.getCardName());
        profileToUpdate.setCardNumber(profileRequest.getCardNumber());
        profileToUpdate.setCardExpiry(profileRequest.getCardExpiry());
        profileToUpdate.setCardCvv(profileRequest.getCardCvv());

        Users savedUser = usersService.save(user);                      // save the user. JPA will handle persisting/merging the profile.

        ProfileRequest returnProfile = new ProfileRequest();            // prepare the response DTO
        returnProfile.setEmail(savedUser.getEmail());
        if (savedUser.getProfile() != null) {
            returnProfile.setFirstName(savedUser.getProfile().getFirstName());
            returnProfile.setLastName(savedUser.getProfile().getLastName());
            returnProfile.setPhone(savedUser.getProfile().getPhone());
            returnProfile.setAddress(savedUser.getProfile().getAddress());
            returnProfile.setLastModifiedDateTime(savedUser.getProfile().getLastModifiedDateTime());
            // removed other sensitive info. (e.g. Credit Card info.)
        }

        return new ResponseEntity<>(returnProfile, HttpStatus.OK);
    }
}