package com.kai.shoppingcart.listener;

public interface LoginListener {
    void loginSuccessfull(String email, String jwtToken, String customerId);
    void loginFailure(String message);
}
