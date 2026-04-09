package com.mitocode.external.visa.dto;

import java.math.BigDecimal;

public record CheckBalanceRequest(String accountId, BigDecimal requiredAmount) {
}
