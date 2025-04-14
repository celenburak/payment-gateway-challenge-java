package com.checkout.payment.gateway.client.model;

public class BankResponse {
  private boolean authorized;
  private String authorizationCode;

  public BankResponse(boolean authorized, String authorizationCode) {
    this.authorized = authorized;
    this.authorizationCode = authorizationCode;
  }

  public boolean isAuthorized() {
    return authorized;
  }

  public String getAuthorizationCode() {
    return authorizationCode;
  }
}