package com.lms.lmssystem.service.impl;

import com.lms.lmssystem.dto.request.*;
import com.lms.lmssystem.dto.response.AuthResponseDto;
import com.lms.lmssystem.dto.response.KeycloakTokenResponseDto;
import com.lms.lmssystem.exception.AuthenticationException;
import com.lms.lmssystem.exception.UserNotFoundException;
import com.lms.lmssystem.mapper.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;


    @Value("${spring.keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${spring.keycloak.realm}")
    private String realm;

    @Value("${spring.keycloak.client-id}")
    private String clientId;

    @Value("${spring.keycloak.client-secret}")
    private String clientSecret;

    @Value("${spring.keycloak.admin-client-id}")
    private String adminClientId;

    @Value("${spring.keycloak.admin-client-secret}")
    private String adminClientSecret;

    private String getTokenUrl() {
        return authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
    }

    private String getAdminUrl() {
        return authServerUrl + "/admin/realms/" + realm;
    }


    public AuthResponseDto authenticate(AuthRequestDto request) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", request.getUsername());
        formData.add("password", request.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

        ResponseEntity<KeycloakTokenResponseDto> response =
                restTemplate.postForEntity(getTokenUrl(), entity, KeycloakTokenResponseDto.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new AuthenticationException("Ошибка аутентификации");
        }

        KeycloakTokenResponseDto body = response.getBody();

        if (body == null){
            throw new AuthenticationException("Пустой ответ от сервера авторизации");
        }

        return new AuthResponseDto(
                body.getAccessToken(),
                body.getRefreshToken()
        );
    }


    public AuthResponseDto refreshToken(RefreshTokenRequestDto request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", request.getRefreshToken());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

        ResponseEntity<KeycloakTokenResponseDto> response =
                restTemplate.postForEntity(getTokenUrl(), entity, KeycloakTokenResponseDto.class);


        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new AuthenticationException("Ошибка обновления токена");
        }

        KeycloakTokenResponseDto body = response.getBody();

        return new AuthResponseDto(
                body.getAccessToken(),
                body.getRefreshToken()
        );
    }


    public void registerUser(RegisterUserRequestDto request) {

        String adminToken = getAdminAccessToken();


        Map<String, Object> user = Map.of(
                "username", request.getUsername(),
                "email", request.getEmail(),
                "firstName", request.getFirstName(),
                "lastName", request.getLastName(),
                "enabled", true,
                "credentials", List.of(Map.of(
                        "type", "password",
                        "value", request.getPassword(),
                        "temporary", false
                ))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(getAdminUrl() + "/users", entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new AuthenticationException("Ошибка создания пользователя");
        }


        assignRolesToUser(request.getUsername(), request.getRoles(), adminToken);
    }


    private String getAdminAccessToken() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", adminClientId);
        formData.add("client_secret", adminClientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                authServerUrl + "/realms/master/protocol/openid-connect/token",
                entity,
                Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new AuthenticationException("Не удалось получить токен администратора");
        }

        Map<String, Object> tokenResponse = response.getBody();
        return (String) tokenResponse.get("access_token");
    }


    private void assignRolesToUser(String username, List<String> roles, String adminToken) {

        ResponseEntity<List> userResponse = restTemplate.exchange(
                getAdminUrl() + "/users?username=" + username,
                HttpMethod.GET,
                new HttpEntity<>(createAuthHeaders(adminToken)),
                List.class
        );

        if (userResponse.getBody() == null || userResponse.getBody().isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден для назначения ролей");
        }

        Map<String, Object> userMap = (Map<String, Object>) userResponse.getBody().get(0);
        String userId = (String) userMap.get("id");

        for (String role : roles) {

            ResponseEntity<Map> roleResponse = restTemplate.exchange(
                    getAdminUrl() + "/roles/" + role,
                    HttpMethod.GET,
                    new HttpEntity<>(createAuthHeaders(adminToken)),
                    Map.class
            );

            Map<String, Object> roleRepresentation = roleResponse.getBody();


            HttpEntity<List<Map<String, Object>>> roleEntity = new HttpEntity<>(List.of(roleRepresentation), createAuthHeaders(adminToken));
            restTemplate.postForEntity(
                    getAdminUrl() + "/users/" + userId + "/role-mappings/realm",
                    roleEntity,
                    String.class
            );
        }
    }


    private HttpHeaders createAuthHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }

    public void updateUser(String username, UpdateUserRequestDto request) {
        String adminToken = getAdminAccessToken();


        ResponseEntity<List> userResponse = restTemplate.exchange(
                getAdminUrl() + "/users?username=" + username,
                HttpMethod.GET,
                new HttpEntity<>(createAuthHeaders(adminToken)),
                List.class
        );

        if (userResponse.getBody() == null || userResponse.getBody().isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        Map<String, Object> userMap = (Map<String, Object>) userResponse.getBody().get(0);
        String userId = (String) userMap.get("id");


        Map<String, Object> updateBody = new java.util.HashMap<>();
        if (request.getEmail() != null) updateBody.put("email", request.getEmail());
        if (request.getFirstName() != null) updateBody.put("firstName", request.getFirstName());
        if (request.getLastName() != null) updateBody.put("lastName", request.getLastName());


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updateBody, createAuthHeaders(adminToken));
        restTemplate.exchange(
                getAdminUrl() + "/users/" + userId,
                HttpMethod.PUT,
                entity,
                String.class
        );


        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            Map<String, Object> credentials = Map.of(
                    "type", "password",
                    "value", request.getPassword(),
                    "temporary", false
            );
            HttpEntity<Map<String, Object>> passwordEntity = new HttpEntity<>(Map.of("credentials", List.of(credentials)), createAuthHeaders(adminToken));
            restTemplate.put(getAdminUrl() + "/users/" + userId, passwordEntity);
        }

    }

    public void updateUserRoles(String username, List<String> roles) {

        String adminToken = getAdminAccessToken();

        if (roles == null || roles.isEmpty()) {
            return;
        }

        assignRolesToUser(username, roles, adminToken);
    }


}
