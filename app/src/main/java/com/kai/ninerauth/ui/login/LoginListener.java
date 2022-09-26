package com.kai.ninerauth.ui.login;

public interface LoginListener {
    void loggedIn();
    void loggedInFailure(String message);
}
