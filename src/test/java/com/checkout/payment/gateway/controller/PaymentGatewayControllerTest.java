package com.checkout.payment.gateway.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.checkout.payment.gateway.client.BankSimulatorClient;
import com.checkout.payment.gateway.client.model.BankRequest;
import com.checkout.payment.gateway.client.model.BankResponse;
import com.checkout.payment.gateway.exception.PaymentNotFoundException;
import com.checkout.payment.gateway.exception.SubmitPaymentException;
import com.checkout.payment.gateway.model.payment_request.PaymentRequest;
import com.checkout.payment.gateway.model.payment_response.ProcessedPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import java.util.UUID;
import com.checkout.payment.gateway.service.PaymentGatewayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentGatewayControllerTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  PaymentsRepository paymentsRepository;
  @Mock
  private BankSimulatorClient bankSimulatorClient;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    /*
    Define mock behavior for BankSimulatorClient based on the last digit of the card number

    If the card number ends on an odd number (1, 3, 5, 7, 9) then the simulator returns an 200 Ok authorized response with a new random authorization_code
    If the card number ends on an even number (2, 4, 6, 8) then the simulator returns an 200 Ok unauthorized response
    If the card number ends on a zero (0) then the simulator returns an error in the form of a 503 Service Unavailable response
     */
    when(bankSimulatorClient.processPayment(any(BankRequest.class)))
        .thenAnswer(invocation -> {
          BankRequest request = invocation.getArgument(0);
          char lastDigit = request.getCardNumber().charAt(request.getCardNumber().length() - 1);
          if (lastDigit == '0') {
            throw new IllegalStateException("Bank simulator is unavailable.");
          }
          boolean authorized = (lastDigit % 2 != 0);
          return new BankResponse(authorized, authorized ? UUID.randomUUID().toString() : null);
        });
  }

  @Test
  void whenPaymentWithIdExistThenCorrectPaymentIsReturned() throws Exception {

    PaymentRequest request = new PaymentRequest();
    request.setAmount(10);
    request.setCurrency("USD");
    request.setExpiryMonth(12);
    request.setExpiryYear(2024);
    request.setCardNumber("4321432143214321");
    ProcessedPaymentResponse payment = ProcessedPaymentResponse.buildAuthorizedResponse(request);

    paymentsRepository.add(payment);

    mvc.perform(MockMvcRequestBuilders.get("/payment/" + payment.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(payment.getStatus().getName()))
        .andExpect(jsonPath("$.card_number_last_four").value(payment.getCardNumberLastFour()))
        .andExpect(jsonPath("$.expiry_month").value(payment.getExpiryMonth()))
        .andExpect(jsonPath("$.expiry_year").value(payment.getExpiryYear()))
        .andExpect(jsonPath("$.currency").value(payment.getCurrency()))
        .andExpect(jsonPath("$.amount").value(payment.getAmount()));
  }

  @Test
  void whenPaymentWithIdDoesNotExistThen404IsReturned() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/payment/" + UUID.randomUUID()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Page not found"));
  }

  @Test
  void whenValidPaymentRequestIsSubmittedThenPaymentIsProcessed() throws Exception {
    // Card number end with odd number is simulating an authorized payment
    String requestBody = """
          {
              "card_number": "4321432143214321",
              "expiry_month": 12,
              "expiry_year": 2099,
              "currency": "USD",
              "amount": 100,
              "cvv": "123"
          }
      """;

      mvc.perform(MockMvcRequestBuilders.post("/payment")
              .contentType("application/json")
              .content(requestBody))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.status").value("Authorized"))
          .andExpect(jsonPath("$.amount").value(100))
          .andExpect(jsonPath("$.currency").value("USD"));
  }

  @Test
  void whenExceptionOccursDuringPaymentSubmissionThenInternalServerErrorIsReturned() throws Exception {
    // Card number 0 is simulating an exception in the bank simulator
    String requestBody = """
          {
              "card_number": "4321432143214320",
              "expiry_month": 12,
              "expiry_year": 2099,
              "currency": "USD",
              "amount": 100,
              "cvv": "123"
          }
      """;

    ResultActions perform = mvc.perform(MockMvcRequestBuilders.post("/payment")
        .contentType("application/json")
        .content(requestBody))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value("Bank simulator is unavailable."));
  }
}
