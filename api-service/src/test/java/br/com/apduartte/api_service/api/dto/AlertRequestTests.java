
package br.com.apduartte.api_service.api.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AlertRequestTests {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    @Test
    void shouldAcceptValidAlertRequest() {
        AlertRequest request = new AlertRequest(
                "PAYMENT_SUSPECTED",
                "{\"transactionId\":\"TX-001\"}"
        );

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void shouldRejectBlankType() {
        AlertRequest request = new AlertRequest(
                " ",
                "{\"transactionId\":\"TX-001\"}"
        );

        assertThat(validator.validate(request))
                .anySatisfy(violation ->
                        assertThat(violation.getPropertyPath().toString())
                                .isEqualTo("type"));
    }

    @Test
    void shouldRejectTypeLongerThan60Characters() {
        AlertRequest request = new AlertRequest(
                "A".repeat(61),
                "{\"transactionId\":\"TX-001\"}"
        );

        assertThat(validator.validate(request))
                .anySatisfy(violation ->
                        assertThat(violation.getPropertyPath().toString())
                                .isEqualTo("type"));
    }

    @Test
    void shouldRejectBlankPayload() {
        AlertRequest request = new AlertRequest(
                "PAYMENT_SUSPECTED",
                " "
        );

        assertThat(validator.validate(request))
                .anySatisfy(violation ->
                        assertThat(violation.getPropertyPath().toString())
                                .isEqualTo("payload"));
    }
}
