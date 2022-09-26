package com.kai.ninerauth.util;

import com.kai.ninerauth.ui.login.LoginListener;
import com.kai.ninerauth.ui.register.RegisterListener;

public class DataSingleton {
    private static DataSingleton instance;

    public static DataSingleton getInstance(){
        if( instance == null ){
            instance = new DataSingleton();
        }
        return instance;
    }

    public DataSingleton() {
    }

    //TODO add OkayHttp calls
    public static void login(String email, String password, LoginListener loginListener) {

    }

    public static void register(String email, String password, String firstName, String lastName, RegisterListener registerListener) {

    }
}
