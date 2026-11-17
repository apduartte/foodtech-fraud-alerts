package br.com.apduartte.api_service.api.dto;

import java.time.Instant;
import java.util.UUID;

import br.com.apduartte.api_service.domain.AlertStatus;

public record AlertResponse(

        UUID id,
        String type,
        AlertStatus status,
        Instant createdAt

) {
}