package com.checkout.payment.gateway.controller;

import com.checkout.payment.gateway.exception.SubmitPaymentException;
import com.checkout.payment.gateway.model.payment_response.PaymentResponse;
import com.checkout.payment.gateway.model.payment_response.ProcessedPaymentResponse;
import com.checkout.payment.gateway.model.payment_request.PaymentRequest;
import com.checkout.payment.gateway.service.PaymentGatewayService;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("api")
public class PaymentGatewayController {

  private final PaymentGatewayService paymentGatewayService;

  public PaymentGatewayController(PaymentGatewayService paymentGatewayService) {
    this.paymentGatewayService = paymentGatewayService;
  }

   @PostMapping("/payment")
   public ResponseEntity<PaymentResponse> submitPaymentRequest(@RequestBody PaymentRequest request){
    try {
      return new ResponseEntity<>(paymentGatewayService.submitPaymentRequest(request), HttpStatus.OK);
    } catch (Exception e) {
      throw new SubmitPaymentException(e.getMessage());
    }
   }

  @GetMapping("/payment/{id}")
  public ResponseEntity<ProcessedPaymentResponse> getPostPaymentEventById(@PathVariable UUID id) {
    return new ResponseEntity<>(paymentGatewayService.getPaymentById(id), HttpStatus.OK);
  }
}
