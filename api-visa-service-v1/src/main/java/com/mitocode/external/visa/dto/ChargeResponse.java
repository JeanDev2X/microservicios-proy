package com.mitocode.external.visa.dto;

import java.math.BigDecimal;

public record ChargeResponse(String accountId, BigDecimal amount, String status) {
}
