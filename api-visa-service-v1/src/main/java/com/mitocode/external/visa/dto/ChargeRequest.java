package com.mitocode.external.visa.dto;

import java.math.BigDecimal;

public record ChargeRequest(String accountId, BigDecimal amount) {
}
