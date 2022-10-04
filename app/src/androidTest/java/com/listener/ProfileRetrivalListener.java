package com.listener;

import com.kai.shoppingcart.model.User;

public interface ProfileRetrivalListener {
    void profileRetrivalSuccessful(User user);
    void profileRetrivalFailure(String message);
}
