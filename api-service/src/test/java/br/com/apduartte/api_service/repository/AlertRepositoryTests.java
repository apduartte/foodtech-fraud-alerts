package br.com.apduartte.api_service.repository;

import br.com.apduartte.api_service.domain.Alert;
import br.com.apduartte.api_service.domain.AlertStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class AlertRepositoryTests {

    @Autowired
    private AlertRepository alertRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldPersistAndRetrieveAlert() {

        // Arrange: cria um alerta
        Alert alert = new Alert(
                "PAYMENT_SUSPECTED",
                "{\"transactionId\":\"TX-001\"}"
        );

        // Act: persiste e força a gravação no banco
        Alert savedAlert = alertRepository.saveAndFlush(alert);

        // Assert: verifica os dados gerados
        assertThat(savedAlert.getId()).isNotNull();
        assertThat(savedAlert.getStatus())
                .isEqualTo(AlertStatus.CREATED);
        assertThat(savedAlert.getCreatedAt()).isNotNull();

        UUID alertId = savedAlert.getId();

        // Limpa o contexto de persistência
        entityManager.clear();

        // Recupera novamente pelo banco
        Alert retrievedAlert = alertRepository.findById(alertId)
                .orElseThrow();

        // Verifica os dados recuperados
        assertThat(retrievedAlert.getId()).isEqualTo(alertId);
        assertThat(retrievedAlert.getType())
                .isEqualTo("PAYMENT_SUSPECTED");
        assertThat(retrievedAlert.getPayload())
                .isEqualTo("{\"transactionId\":\"TX-001\"}");
        assertThat(retrievedAlert.getStatus())
                .isEqualTo(AlertStatus.CREATED);
        assertThat(retrievedAlert.getCreatedAt()).isNotNull();
    }
}
