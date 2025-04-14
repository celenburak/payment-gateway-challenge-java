package com.checkout.payment.gateway.client;

import com.checkout.payment.gateway.client.model.BankRequest;
import com.checkout.payment.gateway.client.model.BankResponse;
import com.checkout.payment.gateway.exception.PaymentNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Component
public class BankSimulatorClient implements AcquiringBankClient {

  @Value("${bank.simulator.url}")
  private String baseUrl;

  private final RestTemplate restTemplate;

  public BankSimulatorClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public BankResponse processPayment(BankRequest request) {
    try {
      return restTemplate.postForObject(baseUrl + "/payments", request, BankResponse.class);
    } catch (HttpClientErrorException.BadRequest e) {
      throw new PaymentNotFoundException("Missing or invalid fields in the payment request.");
    } catch (HttpServerErrorException.ServiceUnavailable e) {
      throw new PaymentNotFoundException("Bank simulator is unavailable.");
    } catch (Exception e) {
      throw new PaymentNotFoundException("An unexpected error occurred while processing the payment.");
    }
  }
}
