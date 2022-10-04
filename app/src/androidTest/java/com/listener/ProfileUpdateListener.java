package com.listener;

public interface ProfileUpdateListener {
    void profileUpdateSuccessful();
    void profileUpdateFailure(String message);
}
