package com.mitocode.payment.client.dto;

public record VisaRequest(String accountId, java.math.BigDecimal amount) {}