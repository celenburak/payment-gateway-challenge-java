package com.checkout.payment.gateway.model.payment_response;

import com.checkout.payment.gateway.enums.PaymentStatus;

public interface PaymentResponse {
  PaymentStatus getStatus();
}
