package com.team_one.expressoh.controllers;

import com.team_one.expressoh.dto.AuthRequest;
import com.team_one.expressoh.dto.AuthResponse;
import com.team_one.expressoh.dto.LoginRequest;
import com.team_one.expressoh.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.team_one.expressoh.service.JWTUtils;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody AuthRequest request) throws Exception {
        AuthResponse response = authService.signUp(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.signIn(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
