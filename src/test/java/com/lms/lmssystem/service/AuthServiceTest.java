package com.lms.lmssystem.service;

import com.lms.lmssystem.dto.request.AuthRequestDto;
import com.lms.lmssystem.dto.response.AuthResponseDto;
import com.lms.lmssystem.dto.response.KeycloakTokenResponseDto;
import com.lms.lmssystem.exception.AuthenticationException;
import com.lms.lmssystem.service.impl.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);


        setField("authServerUrl", "http://localhost:8080");
        setField("realm", "test-realm");
        setField("clientId", "client");
        setField("clientSecret", "secret");
        setField("adminClientId", "admin-client");
        setField("adminClientSecret", "admin-secret");
    }

    private void setField(String fieldName, String value) throws Exception {
        Field field = AuthService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(authService, value);
    }

    @Test
    void authenticate_success() {

        AuthRequestDto request = new AuthRequestDto();
        request.setUsername("user");
        request.setPassword("pass");

        KeycloakTokenResponseDto tokenDto = new KeycloakTokenResponseDto();
        tokenDto.setAccessToken("access");
        tokenDto.setRefreshToken("refresh");

        ResponseEntity<KeycloakTokenResponseDto> response =
                new ResponseEntity<>(tokenDto, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(), eq(KeycloakTokenResponseDto.class)))
                .thenReturn(response);

        AuthResponseDto result = authService.authenticate(request);

        assertEquals("access", result.getAccessToken());
        assertEquals("refresh", result.getRefreshToken());

        verify(restTemplate).postForEntity(anyString(), any(), eq(KeycloakTokenResponseDto.class));
    }

    @Test
    void authenticate_badStatus_shouldThrow() {

        ResponseEntity<KeycloakTokenResponseDto> response =
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        when(restTemplate.postForEntity(anyString(), any(), eq(KeycloakTokenResponseDto.class)))
                .thenReturn(response);

        AuthRequestDto request = new AuthRequestDto();

        assertThrows(AuthenticationException.class,
                () -> authService.authenticate(request));
    }

    @Test
    void authenticate_nullBody_shouldThrow() {

        ResponseEntity<KeycloakTokenResponseDto> response =
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        when(restTemplate.postForEntity(anyString(), any(), eq(KeycloakTokenResponseDto.class)))
                .thenReturn(response);

        AuthRequestDto request = new AuthRequestDto();

        assertThrows(AuthenticationException.class,
                () -> authService.authenticate(request));
    }

    @Test
    void refreshToken_success() {

        KeycloakTokenResponseDto tokenDto = new KeycloakTokenResponseDto();
        tokenDto.setAccessToken("newAccess");
        tokenDto.setRefreshToken("newRefresh");

        ResponseEntity<KeycloakTokenResponseDto> response =
                new ResponseEntity<>(tokenDto, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(), eq(KeycloakTokenResponseDto.class)))
                .thenReturn(response);

        var request = new com.lms.lmssystem.dto.request.RefreshTokenRequestDto();
        request.setRefreshToken("refresh");

        AuthResponseDto result = authService.refreshToken(request);

        assertEquals("newAccess", result.getAccessToken());
        assertEquals("newRefresh", result.getRefreshToken());
    }

    @Test
    void refreshToken_badStatus_shouldThrow() {

        ResponseEntity<KeycloakTokenResponseDto> response =
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        when(restTemplate.postForEntity(anyString(), any(), eq(KeycloakTokenResponseDto.class)))
                .thenReturn(response);

        var request = new com.lms.lmssystem.dto.request.RefreshTokenRequestDto();

        assertThrows(AuthenticationException.class,
                () -> authService.refreshToken(request));
    }
}
