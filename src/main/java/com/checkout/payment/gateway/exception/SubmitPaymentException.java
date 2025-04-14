package com.checkout.payment.gateway.exception;

public class SubmitPaymentException extends RuntimeException{
  public SubmitPaymentException(String message) {
    super(message);
  }
}
