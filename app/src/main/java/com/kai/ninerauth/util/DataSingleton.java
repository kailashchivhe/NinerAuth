package com.kai.ninerauth.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.kai.ninerauth.ui.login.LoginListener;
import com.kai.ninerauth.ui.profile.ProfileListener;
import com.kai.ninerauth.ui.register.RegisterListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataSingleton {
    private static DataSingleton instance;
    private static final OkHttpClient client = new OkHttpClient();
    private static final String TAG = "DataSingleton";

    public static DataSingleton getInstance(){
        if( instance == null ){
            instance = new DataSingleton();
        }
        return instance;
    }

    public DataSingleton() {
    }

    public static void login(String email, String password, LoginListener loginListener) {
         FormBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url("https://node-authenticator-uncc.herokuapp.com/api/auth/login")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JSONObject res = new JSONObject(response.body().string());
                        String status = res.getString("status");
                        JSONObject user = res.getJSONObject("data");
                        String jwtToken = user.getString("token");
                        Log.d(TAG, "onResponse: login status " + status);
                        Log.d(TAG, "user is: " + user);

                        loginListener.loggedIn(jwtToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject loginFailure = new JSONObject(response.body().toString());
                        Log.d(TAG, loginFailure.getString("message"));

                        loginListener.loggedInFailure(loginFailure.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public static void register(String email, String password, String firstName, String lastName, RegisterListener registerListener) {
        FormBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .build();

        Request request = new Request.Builder()
                .url("https://node-authenticator-uncc.herokuapp.com/api/auth/signup")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    JSONObject jsonMessage = null;
                    try {
                        jsonMessage = new JSONObject(response.body().string());
                        Log.d(TAG, jsonMessage.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    registerListener.registered();
                } else {
                    JSONObject jsonErrorMessage = null;
                    try {
                        jsonErrorMessage = new JSONObject(response.body().string());
                        Log.d(TAG, jsonErrorMessage.getString("message"));
                        registerListener.registeredFailure(jsonErrorMessage.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    public static void getProfile(String uid, ProfileListener profileListener) {
        //TODO add OkayHttp GET call when API is implemented

    }
}
