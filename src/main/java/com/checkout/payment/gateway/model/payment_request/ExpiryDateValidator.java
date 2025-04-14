package com.checkout.payment.gateway.model.payment_request;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ExpiryDateValidator implements ConstraintValidator<ValidExpiryDate, PaymentRequest> {

    @Override
    public boolean isValid(PaymentRequest request, ConstraintValidatorContext context) {
        if (request.getExpiryMonth() == null || request.getExpiryYear() == null) {
            return false;
        }

        // Get the current date
        LocalDate today = LocalDate.now();

        // Create a LocalDate object for the expiry date (last day of the expiry month)
        LocalDate expiryDate = LocalDate.of(request.getExpiryYear(), request.getExpiryMonth(), 1)
                .withDayOfMonth(LocalDate.of(request.getExpiryYear(), request.getExpiryMonth(), 1).lengthOfMonth());

        // Check if the expiry date is in the future
        return expiryDate.isAfter(today);
    }
}