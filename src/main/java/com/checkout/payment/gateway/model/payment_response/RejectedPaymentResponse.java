package com.checkout.payment.gateway.model.payment_response;

import com.checkout.payment.gateway.enums.PaymentStatus;

public class RejectedPaymentResponse implements PaymentResponse {

  private PaymentStatus status;
  private String rejectionReason;

  public RejectedPaymentResponse() {
    setStatus(PaymentStatus.REJECTED);
  }

  public PaymentStatus getStatus() {
    return status;
  }

  public RejectedPaymentResponse setStatus(PaymentStatus status) {
    this.status = status;
    return this;
  }

  public String getRejectionReason() {
    return rejectionReason;
  }

  public RejectedPaymentResponse setRejectionReason(String rejectionReason) {
    this.rejectionReason = rejectionReason;
    return this;
  }

  @Override
  public String toString() {
    return "RejectedPaymentResponse{" +
        "AbstractPaymentResponse=" + super.toString() +
        "rejectionReason='" + rejectionReason + '\'' +
        '}';
  }
}
