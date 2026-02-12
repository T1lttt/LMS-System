package com.lms.lmssystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateUserRequestDto {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private List<String> roles;

}
