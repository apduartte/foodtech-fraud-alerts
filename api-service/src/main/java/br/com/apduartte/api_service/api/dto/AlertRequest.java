package br.com.apduartte.api_service.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlertRequest(

        @NotBlank(message = "Alert type is required")
        @Size(max = 60, message = "Alert type must not exceed 60 characters")
        String type,

        @NotBlank(message = "Alert payload is required")
        String payload

) {
}
