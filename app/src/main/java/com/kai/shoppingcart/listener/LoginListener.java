package com.kai.shoppingcart.listener;

public interface LoginListener {
    void loginSuccessfull(String email, String jwtToken);
    void loginFailure(String message);
}
