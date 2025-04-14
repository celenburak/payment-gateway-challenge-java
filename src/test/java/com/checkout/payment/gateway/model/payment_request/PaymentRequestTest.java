package com.checkout.payment.gateway.model.payment_request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void testValidExpiryDate() {
        PaymentRequest request = new PaymentRequest();
        request.setCardNumber("1234567890123456");
        request.setExpiryMonth(12);
        request.setExpiryYear(2026); // Future year
        request.setCurrency("USD");
        request.setAmount(1000);
        request.setCvv("123");

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "Expected no validation errors for a valid expiry date");
    }

    @Test
    void testInvalidExpiryDate() {
        PaymentRequest request = new PaymentRequest();
        request.setCardNumber("1234567890123456");
        request.setExpiryMonth(12);
        request.setExpiryYear(2023); // Past year
        request.setCurrency("USD");
        request.setAmount(1000);
        request.setCvv("123");

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Expected validation errors for an invalid expiry date");
    }

    @Test
    void testMissingRequiredFields() {
        PaymentRequest request = new PaymentRequest();
        // No fields are set

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Expected validation errors for missing required fields");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Card number is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Expiry month is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Expiry year is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Currency is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Amount is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("CVV is required")));
    }

    @Test
    void testInvalidCardNumberFormat() {
        PaymentRequest request = new PaymentRequest();
        request.setCardNumber("12345"); // Invalid length
        request.setExpiryMonth(12);
        request.setExpiryYear(2026);
        request.setCurrency("USD");
        request.setAmount(1000);
        request.setCvv("123");

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Expected validation errors for invalid card number format");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Card number must be between 14-19 numeric characters")));
    }

    @Test
    void testInvalidCvvFormat() {
        PaymentRequest request = new PaymentRequest();
        request.setCardNumber("1234567890123456");
        request.setExpiryMonth(12);
        request.setExpiryYear(2026);
        request.setCurrency("USD");
        request.setAmount(1000);
        request.setCvv("12"); // Invalid length

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Expected validation errors for invalid CVV format");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("CVV must be 3-4 numeric characters")));
    }

    @Test
    void testInvalidCurrency() {
        PaymentRequest request = new PaymentRequest();
        request.setCardNumber("1234567890123456");
        request.setExpiryMonth(12);
        request.setExpiryYear(2026);
        request.setCurrency("JPY"); // Unsupported currency
        request.setAmount(1000);
        request.setCvv("123");

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Expected validation errors for unsupported currency");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Currency must be one of USD, GBP, or EUR")));
    }

    @Test
    void testInvalidAmount() {
        PaymentRequest request = new PaymentRequest();
        request.setCardNumber("1234567890123456");
        request.setExpiryMonth(12);
        request.setExpiryYear(2026);
        request.setCurrency("USD");
        request.setAmount(0); // Invalid amount
        request.setCvv("123");

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Expected validation errors for invalid amount");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Amount must be greater than 0")));
    }
}
