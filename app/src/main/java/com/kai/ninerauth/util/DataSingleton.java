package com.kai.ninerauth.util;

import android.content.Context;

import com.kai.ninerauth.ui.login.LoginListener;
import com.kai.ninerauth.ui.register.RegisterListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DataSingleton {
    private static DataSingleton instance;
    private static final OkHttpClient client = new OkHttpClient();

    public static DataSingleton getInstance(){
        if( instance == null ){
            instance = new DataSingleton();
        }
        return instance;
    }

    public DataSingleton() {
    }

    //TODO add OkayHttp calls
    public static void login(String email, String password, LoginListener loginListener) throws Exception{
        JSONObject loginObject = new JSONObject();
        try{
            loginObject.put("email", email);
            loginObject.put("password", password);
        } catch(JSONException jse) {
            jse.printStackTrace();
        }

        final MediaType MEDIA_TYPE_JSON
                = MediaType.parse("application/json; charset=utf-8");

        String postBody = loginObject.toString();

        Request request = new Request.Builder()
                .url("https://node-authenticator-uncc.herokuapp.com/api/auth/login")
                .post(RequestBody.create(postBody, MEDIA_TYPE_JSON))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()){
                loginListener.loggedInFailure(response.message());
                throw new IOException("Unexpected code " + response);
            } else {
                loginListener.loggedIn();
            }

            System.out.println(response.body().string());
        }
    }

    public static void register(String email, String password, String firstName, String lastName, RegisterListener registerListener) throws  Exception{
        JSONObject registerObject = new JSONObject();
        try{
            registerObject.put("email", email);
            registerObject.put("password", password);
        } catch(JSONException jse) {
            jse.printStackTrace();
        }

        final MediaType MEDIA_TYPE_JSON
                = MediaType.parse("application/json; charset=utf-8");

        String postBody = registerObject.toString();

        Request request = new Request.Builder()
                .url("https://node-authenticator-uncc.herokuapp.com/api/auth/signup")
                .post(RequestBody.create(postBody, MEDIA_TYPE_JSON))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                registerListener.registeredFailure(response.message());
                throw new IOException("Unexpected code " + response);
            } else {
                registerListener.registered();
            }

            System.out.println(response.body().string());
        }
    }

    public static void getProfile() {
        
    }
}
