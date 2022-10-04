package com.kai.shoppingcart.model;

public class LoginData {
    String email;
    String jwtToken;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public LoginData(String email, String jwtToken) {
        this.email = email;
        this.jwtToken = jwtToken;
    }
}
