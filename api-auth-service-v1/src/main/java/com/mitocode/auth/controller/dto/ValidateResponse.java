package com.mitocode.auth.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ValidateResponse {
    private boolean valid;
    private Long userId;
    private String username;
    private List<String> roles;
}