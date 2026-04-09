package com.mitocode.external.visa.dto;

import java.math.BigDecimal;

public record CheckBalanceResponse(String accountId, BigDecimal balance, boolean sufficient) {
}
