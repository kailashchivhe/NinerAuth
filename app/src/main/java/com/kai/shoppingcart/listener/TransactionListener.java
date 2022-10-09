package com.kai.shoppingcart.listener;

public interface TransactionListener {
    void transactionSuccessfull();
    void transactionFailure(String message);
}
