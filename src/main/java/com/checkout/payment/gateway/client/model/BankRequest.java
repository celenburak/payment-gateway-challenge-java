package com.checkout.payment.gateway.client.model;

import com.checkout.payment.gateway.model.payment_request.PaymentRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class BankRequest {
  @JsonProperty("card_number")
  private String cardNumber;
  @JsonProperty("expiry_date")
  private String expiryDate;
  private String currency;
  private int amount;
  private String cvv;

  public static BankRequest fromPaymentRequest(PaymentRequest paymentRequest) {
    return new BankRequest()
        .setCardNumber(paymentRequest.getCardNumber())
        .setExpiryDate(String.format("%02d/%d", paymentRequest.getExpiryMonth(), paymentRequest.getExpiryYear()))
        .setCurrency(paymentRequest.getCurrency())
        .setAmount(paymentRequest.getAmount())
        .setCvv(paymentRequest.getCvv());
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public BankRequest setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
    return this;
  }

  public String getExpiryDate() {
    return expiryDate;
  }

  public BankRequest setExpiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
    return this;
  }

  public String getCurrency() {
    return currency;
  }

  public BankRequest setCurrency(String currency) {
    this.currency = currency;
    return this;
  }

  public int getAmount() {
    return amount;
  }

  public BankRequest setAmount(int amount) {
    this.amount = amount;
    return this;
  }

  public String getCvv() {
    return cvv;
  }

  public BankRequest setCvv(String cvv) {
    this.cvv = cvv;
    return this;
  }

  @Override
  public String toString() {
    return "BankSimulatorRequest{" +
        "cardNumber='" + cardNumber + '\'' +
        ", expiryDate='" + expiryDate + '\'' +
        ", currency='" + currency + '\'' +
        ", amount=" + amount +
        ", cvv='" + cvv + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass())
      return false;
    BankRequest that = (BankRequest) o;
    return getAmount() == that.getAmount() && Objects.equals(getCardNumber(), that.getCardNumber())
        && Objects.equals(getExpiryDate(), that.getExpiryDate()) && Objects.equals(getCurrency(),
        that.getCurrency()) && Objects.equals(getCvv(), that.getCvv());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCardNumber(), getExpiryDate(), getCurrency(), getAmount(), getCvv());
  }
}
