package com.mitocode.auth.controller.dto;

import lombok.Data;

@Data
public class RefreshRequest {
    private String refreshToken;
}
