package com.kai.ninerauth.listener;

import com.kai.ninerauth.Model.User;

public interface ProfileRetrivalListener {
    void profileRetrivalSuccessful(User user);
    void profileRetrivalFailure(String message);
}
