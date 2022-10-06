package com.kai.shoppingcart.model;

public class LoginData {
    String id;
    String jwtToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public LoginData(String id, String jwtToken) {
        this.id = id;
        this.jwtToken = jwtToken;
    }
}
