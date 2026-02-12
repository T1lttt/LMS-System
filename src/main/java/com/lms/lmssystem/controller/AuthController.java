package com.lms.lmssystem.controller;

import com.lms.lmssystem.dto.*;
import com.lms.lmssystem.service.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto request){
        AuthResponseDto responseDto = authService.authenticate(request);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto request) {
        AuthResponseDto response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserRequestDto request) {
        authService.registerUser(request);
        return ResponseEntity.ok("User created successfully");
    }

    @PutMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserRequestDto request,
                                             @AuthenticationPrincipal Jwt principal) {

        String currentUsername = principal.getClaim("preferred_username");


        boolean isAdmin = principal.getClaimAsStringList("realm_access.roles").contains("ROLE_ADMIN");

        authService.updateUser(currentUsername, request, isAdmin);
        return ResponseEntity.ok("User updated successfully");
    }


}
