package com.kai.ninerauth.ui.login;

public interface LoginListener {
    void loggedIn(String jwtToken);
    void loggedInFailure(String message);
}
