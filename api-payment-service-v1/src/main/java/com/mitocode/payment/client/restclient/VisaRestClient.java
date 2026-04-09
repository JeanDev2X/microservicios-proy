package com.mitocode.payment.client.restclient;

import com.mitocode.payment.client.dto.VisaRequest;
import com.mitocode.payment.client.dto.VisaResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class VisaRestClient {

    private final RestClient restClient;

    // IMPORTANTE: Inyectamos el RestClient, no el Builder ni la clase Config
    // Inyectamos el bean que ahora se llama visaClient
    public VisaRestClient(RestClient visaClient) {
        this.restClient = visaClient;
    }

    @CircuitBreaker(name = "visaService", fallbackMethod = "fallbackProcessPayment")
    public VisaResponse processPayment(VisaRequest request) {
        return restClient.post()
                .uri("/api/visa/account/charge")
                .body(request)
                .retrieve()
                .body(VisaResponse.class);
    }

    // MÉTODO FALLBACK: Corregido con los tipos de datos correctos
    public VisaResponse fallbackProcessPayment(VisaRequest request, Throwable t) {
        System.err.println("🛡️ Circuit Breaker activado. Motivo: " + t.getMessage());
        return new VisaResponse(
                request.accountId(),
                java.math.BigDecimal.ZERO,
                "FAILED"
        );
    }

}
