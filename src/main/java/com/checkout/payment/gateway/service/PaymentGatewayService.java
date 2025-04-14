package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.client.BankSimulatorClient;
import com.checkout.payment.gateway.client.model.BankRequest;
import com.checkout.payment.gateway.client.model.BankResponse;
import com.checkout.payment.gateway.exception.PaymentNotFoundException;
import com.checkout.payment.gateway.exception.SubmitPaymentException;
import com.checkout.payment.gateway.model.payment_response.PaymentResponse;
import com.checkout.payment.gateway.model.payment_response.ProcessedPaymentResponse;
import com.checkout.payment.gateway.model.payment_request.PaymentRequest;
import com.checkout.payment.gateway.model.payment_response.RejectedPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import java.util.Set;
import java.util.UUID;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayService {

  private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayService.class);

  @Autowired
  private PaymentsRepository paymentsRepository;
  @Autowired
  private BankSimulatorClient bankSimulatorClient;
  @Autowired
  private Validator validator;

  public ProcessedPaymentResponse getPaymentById(UUID id) {
    LOG.debug("Requesting access to to payment with ID {}", id);
    return paymentsRepository.get(id).orElseThrow(() -> new PaymentNotFoundException("Invalid ID"));
  }

  public PaymentResponse submitPaymentRequest(PaymentRequest paymentRequest) {
    LOG.debug("Processing payment request: {}", paymentRequest);

    Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(paymentRequest);
    if (!violations.isEmpty()) {
      LOG.debug("Payment rejected due to validation errors: {}", violations);
      return new RejectedPaymentResponse()
          .setRejectionReason(buildErrorMessageForRejectedPayment(violations));
    }

    ProcessedPaymentResponse paymentResponse = submitPaymentRequestToBank(paymentRequest);

    paymentsRepository.add(paymentResponse);
    LOG.debug("Payment response added to repository: {}", paymentResponse);

    return paymentResponse;
  }

  private ProcessedPaymentResponse submitPaymentRequestToBank(PaymentRequest paymentRequest) {
    BankResponse bankResponse = bankSimulatorClient.processPayment(
        BankRequest.fromPaymentRequest(paymentRequest));
    LOG.debug("Received bank response: {}", bankResponse);

    if (bankResponse == null) {
      throw new SubmitPaymentException("Bank response is null");
    }

    if(bankResponse.isAuthorized()) {
      LOG.debug("Payment authorized");
      return ProcessedPaymentResponse.buildAuthorizedResponse(paymentRequest);
    } else {
      LOG.debug("Payment unauthorized");
      return ProcessedPaymentResponse.buildDeclinedResponse(paymentRequest);
    }
  }

  private String buildErrorMessageForRejectedPayment(
      Set<ConstraintViolation<PaymentRequest>> violations) {
    StringBuilder errorMessage = new StringBuilder("Validation failed for payment request: ");
    for (ConstraintViolation<PaymentRequest> violation : violations) {
      errorMessage.append(violation.getPropertyPath()).append(" ").append(violation.getMessage()).append("; ");
    }
    return errorMessage.toString();
  }
}
