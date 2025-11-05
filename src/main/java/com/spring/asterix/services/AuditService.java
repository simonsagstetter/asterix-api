package com.spring.asterix.services;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AuditService {

    public AuditService() {
    }

    public Instant getCurrentTimestamp() {
        return Instant.now();
    }
}
