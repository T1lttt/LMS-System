package com.lms.lmssystem.controller;

import com.lms.lmssystem.dto.request.*;
import com.lms.lmssystem.dto.response.AuthResponseDto;
import com.lms.lmssystem.service.impl.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private Jwt jwt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        AuthRequestDto request = new AuthRequestDto();
        request.setUsername("user");
        request.setPassword("pass");

        AuthResponseDto responseDto = new AuthResponseDto("access-token", "refresh-token");

        when(authService.authenticate(any(AuthRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<AuthResponseDto> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("access-token", response.getBody().getAccessToken());
        assertEquals("refresh-token", response.getBody().getRefreshToken());

        verify(authService).authenticate(request);
    }

    @Test
    void testRefreshToken() {
        RefreshTokenRequestDto request = new RefreshTokenRequestDto();
        request.setRefreshToken("refresh-token");

        AuthResponseDto responseDto = new AuthResponseDto("new-access", "new-refresh");

        when(authService.refreshToken(any(RefreshTokenRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<AuthResponseDto> response = authController.refreshToken(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("new-access", response.getBody().getAccessToken());
        assertEquals("new-refresh", response.getBody().getRefreshToken());

        verify(authService).refreshToken(request);
    }

    @Test
    void testRegisterUser() {
        RegisterUserRequestDto request = new RegisterUserRequestDto();
        request.setUsername("newuser");

        doNothing().when(authService).registerUser(request);

        ResponseEntity<String> response = authController.registerUser(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User created successfully", response.getBody());

        verify(authService).registerUser(request);
    }

    @Test
    void testUpdateUser() {
        UpdateUserRequestDto request = new UpdateUserRequestDto();
        request.setEmail("test@example.com");

        when(jwt.getClaim("preferred_username")).thenReturn("user1");
        doNothing().when(authService).updateUser("user1", request);

        ResponseEntity<String> response = authController.updateUser(request, jwt);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User updated successfully", response.getBody());

        verify(authService).updateUser("user1", request);
    }

    @Test
    void testUpdateUserRoles() {
        String username = "user1";
        List<String> roles = List.of("ROLE_ADMIN");

        doNothing().when(authService).updateUserRoles(username, roles);

        ResponseEntity<String> response = authController.updateUserRoles(username, roles);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Roles updated successfully", response.getBody());

        verify(authService).updateUserRoles(username, roles);
    }
}