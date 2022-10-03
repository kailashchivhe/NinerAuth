package com.kai.ninerauth.listener;

public interface ProfileRetrivalListener {
    void profileRetrivalSuccessful();
    void profileRetrivalFailure(String message);
}
