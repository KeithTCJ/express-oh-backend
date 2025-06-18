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
import java.util.Map;


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

        Profile profile = user.getProfile();
        if (profile != null) {
            returnProfile.setFirstName(profile.getFirstName());
            returnProfile.setLastName(profile.getLastName());
            returnProfile.setPhone(profile.getPhone());
            returnProfile.setAddress(profile.getAddress());
            returnProfile.setLastModifiedDateTime(profile.getLastModifiedDateTime());
        }

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
    @GetMapping("/profile/cardinfo")
    public ResponseEntity<Map<String, String>> getMaskedCardInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Users user = usersService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No user found."));

        Profile profile = user.getProfile();

        if (profile == null || profile.getCardNumber() == null || profile.getCardNumber().length() < 4) {
            // No card info or invalid card number
            return ResponseEntity.ok(Map.of(
                    "maskedCardNumber", "",
                    "cardName", profile != null ? profile.getCardName() : "",
                    "expiryDate", profile != null ? profile.getCardExpiry() : ""
            ));
        }

        String cardNumber = profile.getCardNumber();
        String last4Digits = cardNumber.substring(cardNumber.length() - 4);
        String maskedNumber = "**** **** **** " + last4Digits;

        return ResponseEntity.ok(Map.of(
                "maskedCardNumber", maskedNumber,
                "cardName", profile.getCardName(),
                "expiryDate", profile.getCardExpiry() != null ? profile.getCardExpiry() : ""
        ));
    }
}