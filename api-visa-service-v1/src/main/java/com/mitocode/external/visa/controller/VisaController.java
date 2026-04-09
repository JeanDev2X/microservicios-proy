package com.mitocode.external.visa.controller;

import com.mitocode.external.visa.chaos.ChaosConfig;
import com.mitocode.external.visa.chaos.ChaosState;
import com.mitocode.external.visa.chaos.ChaosType;
import com.mitocode.external.visa.dto.ChargeRequest;
import com.mitocode.external.visa.dto.ChargeResponse;
import com.mitocode.external.visa.dto.CheckBalanceRequest;
import com.mitocode.external.visa.dto.CheckBalanceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/account")
public class VisaController {

    private final ChaosState chaosState = new ChaosState();

    @PostMapping("/chaos")
    public ResponseEntity<Void> configureChaos(@RequestBody ChaosConfig chaosConfig) {
        chaosState.update(chaosConfig);
        return ResponseEntity.ok().build();
    }

    private void applyChaos() throws InterruptedException {
        ChaosConfig chaos = chaosState.getConfig();

        if (!chaos.enabled() || chaos.type() == ChaosType.NONE) {
            return;
        }

        if (chaos.type() == ChaosType.DELAY) {
            Thread.sleep(chaos.delayMs());
        }

        if (chaos.type() == ChaosType.ERROR) {
            throw new RuntimeException("Chaos: forced error");
        }

        if (chaos.type() == ChaosType.ERROR_N_TIMES && chaosState.shouldFail()) {
            throw new RuntimeException("Chaos: temporary error");
        }
    }

    // ================= BUSINESS =================

    @PostMapping("/check-balance")
    public ResponseEntity<CheckBalanceResponse> checkBalance(@RequestBody CheckBalanceRequest request) throws InterruptedException {

        applyChaos();

        BigDecimal balance = BigDecimal.valueOf(500);
        boolean sufficient = balance.compareTo(request.requiredAmount()) >= 0;

        return ResponseEntity.ok(
                new CheckBalanceResponse(request.accountId(), balance, sufficient)
        );
    }

    @PostMapping("/charge")
    public ResponseEntity<ChargeResponse> charge(@RequestBody ChargeRequest request) throws InterruptedException {

        applyChaos();

        if (request.amount().compareTo(BigDecimal.valueOf(500)) > 0) {
            return ResponseEntity.badRequest()
                    .body(new ChargeResponse(request.accountId(), request.amount(), "INSUFFICIENT_FUNDS"));
        }

        return ResponseEntity.ok(
                new ChargeResponse(request.accountId(), request.amount(), "SUCCESS")
        );
    }
}
