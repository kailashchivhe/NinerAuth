package com.kai.shoppingcart.listener;

public interface TokenGetListener {
    void tokenGetSuccessfull(String brainTreeToken);
    void tokenGetFailure(String message);
}
