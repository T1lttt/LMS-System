package com.lms.lmssystem.controller;

import com.lms.lmssystem.dto.request.*;
import com.lms.lmssystem.dto.response.AuthResponseDto;
import com.lms.lmssystem.service.impl.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Authentication", description = "Аутентификация и управление пользователями")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Вход пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешная аутентификация"),
            @ApiResponse(responseCode = "401", description = "Неверный логин или пароль")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto request) {
        AuthResponseDto responseDto = authService.authenticate(request);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Обновление токена доступа")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Токен обновлён"),
            @ApiResponse(responseCode = "401", description = "Неверный или просроченный refresh token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto request) {
        AuthResponseDto response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Регистрация нового пользователя (только ADMIN)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь создан"),
            @ApiResponse(responseCode = "403", description = "Нет прав для регистрации")
    })
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserRequestDto request) {
        authService.registerUser(request);
        return ResponseEntity.ok("User created successfully");
    }

    @Operation(summary = "Обновление данных текущего пользователя (аутентифицированный пользователь)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные обновлены"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован")
    })
    @PutMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserRequestDto request,
                                             @AuthenticationPrincipal Jwt principal) {

        String currentUsername = principal.getClaim("preferred_username");
        authService.updateUser(currentUsername, request);
        return ResponseEntity.ok("User updated successfully");
    }

    @Operation(summary = "Обновление ролей пользователя (только ADMIN)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Роли обновлены"),
            @ApiResponse(responseCode = "403", description = "Нет прав для обновления ролей")
    })
    @PutMapping("/admin/{username}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUserRoles(@PathVariable String username,
                                                  @RequestBody List<String> roles) {

        authService.updateUserRoles(username, roles);
        return ResponseEntity.ok("Roles updated successfully");
    }
}