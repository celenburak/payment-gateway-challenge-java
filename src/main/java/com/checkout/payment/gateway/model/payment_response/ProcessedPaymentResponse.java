package com.checkout.payment.gateway.model.payment_response;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.payment_request.PaymentRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.UUID;

public class ProcessedPaymentResponse implements PaymentResponse {

  private UUID id;
  private PaymentStatus status;
  @JsonProperty("card_number_last_four")
  private int cardNumberLastFour;
  @JsonProperty("expiry_month")
  private int expiryMonth;
  @JsonProperty("expiry_year")
  private int expiryYear;
  private String currency;
  private int amount;

  protected ProcessedPaymentResponse() {}

  public static ProcessedPaymentResponse buildAuthorizedResponse(PaymentRequest request) {
    return new ProcessedPaymentResponse()
        .setId(generateRandomUUID())
        .setStatus(PaymentStatus.AUTHORIZED)
        .setPaymentDetails(request);
  }

  public static ProcessedPaymentResponse buildDeclinedResponse(PaymentRequest request) {
    return new ProcessedPaymentResponse()
        .setId(generateRandomUUID())
        .setStatus(PaymentStatus.DECLINED)
        .setPaymentDetails(request);
  }

  protected ProcessedPaymentResponse setPaymentDetails(PaymentRequest request) {
    return this.setCardNumberLastFour(request.getCardNumberLastFour())
        .setExpiryMonth(request.getExpiryMonth())
        .setExpiryYear(request.getExpiryYear())
        .setCurrency(request.getCurrency())
        .setAmount(request.getAmount());
  }

  protected static UUID generateRandomUUID() {
    return UUID.randomUUID();
  }

  public UUID getId() {
    return id;
  }

  public ProcessedPaymentResponse setId(UUID id) {
    this.id = id;
    return this;
  }

  public PaymentStatus getStatus() {
    return status;
  }

  public ProcessedPaymentResponse setStatus(PaymentStatus status) {
    this.status = status;
    return this;
  }

  public int getCardNumberLastFour() {
    return cardNumberLastFour;
  }

  public ProcessedPaymentResponse setCardNumberLastFour(int cardNumberLastFour) {
    this.cardNumberLastFour = cardNumberLastFour;
    return this;
  }

  public int getExpiryMonth() {
    return expiryMonth;
  }

  public ProcessedPaymentResponse setExpiryMonth(int expiryMonth) {
    this.expiryMonth = expiryMonth;
    return this;
  }

  public int getExpiryYear() {
    return expiryYear;
  }

  public ProcessedPaymentResponse setExpiryYear(int expiryYear) {
    this.expiryYear = expiryYear;
    return this;
  }

  public String getCurrency() {
    return currency;
  }

  public ProcessedPaymentResponse setCurrency(String currency) {
    this.currency = currency;
    return this;
  }

  public int getAmount() {
    return amount;
  }

  public ProcessedPaymentResponse setAmount(int amount) {
    this.amount = amount;
    return this;
  }

  @Override
  public String toString() {
    return "ProcessedPaymentResponse{" +
        "id=" + id +
        ", status=" + status +
        ", cardNumberLastFour=" + cardNumberLastFour +
        ", expiryMonth=" + expiryMonth +
        ", expiryYear=" + expiryYear +
        ", currency='" + currency + '\'' +
        ", amount=" + amount +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass())
      return false;
    ProcessedPaymentResponse that = (ProcessedPaymentResponse) o;
    return getCardNumberLastFour() == that.getCardNumberLastFour()
        && getExpiryMonth() == that.getExpiryMonth() && getExpiryYear() == that.getExpiryYear()
        && getAmount() == that.getAmount() && Objects.equals(getId(), that.getId())
        && getStatus() == that.getStatus() && Objects.equals(getCurrency(), that.getCurrency());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getStatus(), getCardNumberLastFour(), getExpiryMonth(),
        getExpiryYear(), getCurrency(), getAmount());
  }
}
