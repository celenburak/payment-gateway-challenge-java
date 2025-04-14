package com.checkout.payment.gateway.client;

import com.checkout.payment.gateway.client.model.BankRequest;
import com.checkout.payment.gateway.client.model.BankResponse;

public interface AcquiringBankClient {

  BankResponse processPayment(BankRequest request);
}
