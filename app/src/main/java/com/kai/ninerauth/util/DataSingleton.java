package com.kai.ninerauth.util;

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
}
