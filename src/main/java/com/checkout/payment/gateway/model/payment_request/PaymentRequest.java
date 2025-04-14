package com.checkout.payment.gateway.model.payment_request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import java.util.Objects;


@ValidExpiryDate
public class PaymentRequest {

    @JsonProperty("card_number")
    @NotNull(message = "Card number is required")
    @Pattern(regexp = "\\d{14,19}", message = "Card number must be between 14-19 numeric characters")
    private String cardNumber;

    @JsonProperty("expiry_month")
    @NotNull(message = "Expiry month is required")
    @Min(value = 1, message = "Expiry month must be between 1 and 12")
    @Max(value = 12, message = "Expiry month must be between 1 and 12")
    private Integer expiryMonth;

    @JsonProperty("expiry_year")
    @NotNull(message = "Expiry year is required")
    private Integer expiryYear;

    @NotNull(message = "Currency is required")
    @Pattern(regexp = "USD|GBP|EUR", message = "Currency must be one of USD, GBP, or EUR")
    private String currency;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be greater than 0")
    private Integer amount;

    @NotNull(message = "CVV is required")
    @Pattern(regexp = "\\d{3,4}", message = "CVV must be 3-4 numeric characters")
    private String cvv;

    
    public String getCardNumber() {
        return cardNumber;
    }

    public Integer getCardNumberLastFour() {
        if (cardNumber != null && cardNumber.length() >= 4) {
            return Integer.valueOf(cardNumber.substring(cardNumber.length() - 4));
        }
        return null;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(Integer expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public Integer getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(Integer expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "cardNumberLast4='" + getCardNumberLastFour() + '\'' +
                ", expiryMonth=" + expiryMonth +
                ", expiryYear=" + expiryYear +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                ", cvv='" + cvv + '\'' +
                '}';
    }

    // hashCode Method
    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, expiryMonth, expiryYear, currency, amount, cvv);
    }

    // equals Method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PaymentRequest that = (PaymentRequest) obj;
        return Objects.equals(cardNumber, that.cardNumber) &&
                Objects.equals(expiryMonth, that.expiryMonth) &&
                Objects.equals(expiryYear, that.expiryYear) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(cvv, that.cvv);
    }
}