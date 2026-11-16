package br.com.apduartte.api_service.repository;

import br.com.apduartte.api_service.domain.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlertRepository extends JpaRepository<Alert, UUID> {
}
