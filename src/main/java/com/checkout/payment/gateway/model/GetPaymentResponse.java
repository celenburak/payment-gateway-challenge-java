package com.checkout.payment.gateway.model;

import com.checkout.payment.gateway.enums.PaymentStatus;
import java.util.UUID;

public class GetPaymentResponse {
  private UUID id;
  private PaymentStatus status;
  private int cardNumberLastFour;
  private int expiryMonth;
  private int expiryYear;
  private String currency;
  private int amount;

  public UUID getId() {
    return id;
  }

  public GetPaymentResponse setId(UUID id) {
    this.id = id;
    return this;
  }

  public PaymentStatus getStatus() {
    return status;
  }

  public GetPaymentResponse setStatus(PaymentStatus status) {
    this.status = status;
    return this;
  }

  public int getCardNumberLastFour() {
    return cardNumberLastFour;
  }

  public GetPaymentResponse setCardNumberLastFour(int cardNumberLastFour) {
    this.cardNumberLastFour = cardNumberLastFour;
    return this;
  }

  public int getExpiryMonth() {
    return expiryMonth;
  }

  public GetPaymentResponse setExpiryMonth(int expiryMonth) {
    this.expiryMonth = expiryMonth;
    return this;
  }

  public int getExpiryYear() {
    return expiryYear;
  }

  public GetPaymentResponse setExpiryYear(int expiryYear) {
    this.expiryYear = expiryYear;
    return this;
  }

  public String getCurrency() {
    return currency;
  }

  public GetPaymentResponse setCurrency(String currency) {
    this.currency = currency;
    return this;
  }

  public int getAmount() {
    return amount;
  }

  public GetPaymentResponse setAmount(int amount) {
    this.amount = amount;
    return this;
  }

  @Override
  public String toString() {
    return "GetPaymentResponse{" +
        "id=" + id +
        ", status=" + status +
        ", cardNumberLastFour=" + cardNumberLastFour +
        ", expiryMonth=" + expiryMonth +
        ", expiryYear=" + expiryYear +
        ", currency='" + currency + '\'' +
        ", amount=" + amount +
        '}';
  }
}
