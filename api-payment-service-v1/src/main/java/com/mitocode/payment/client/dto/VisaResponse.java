package com.mitocode.payment.client.dto;

public record VisaResponse(String accountId, java.math.BigDecimal amount, String status) {}