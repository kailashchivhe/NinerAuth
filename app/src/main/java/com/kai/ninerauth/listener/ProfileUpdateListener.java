package com.kai.ninerauth.listener;

public interface ProfileUpdateListener {
    void profileUpdateSuccessful();
    void profileUpdateFailure(String message);
}
