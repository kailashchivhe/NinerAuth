package com.kai.shoppingcart.util;

import androidx.annotation.NonNull;

import com.kai.shoppingcart.listener.LoginListener;
import com.kai.shoppingcart.listener.RegisterationListener;
import com.kai.shoppingcart.listener.ProfileRetrivalListener;
import com.kai.shoppingcart.listener.ProfileUpdateListener;
import com.kai.shoppingcart.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APIHelper {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String TAG = "APIHelper";

    public APIHelper() {
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
                        JSONObject user = res.getJSONObject("data");
                        String userEmail = user.getString("email");
                        String jwtToken = user.getString("accessToken");

                        loginListener.loginSuccessfull(userEmail, jwtToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject loginFailure = new JSONObject(response.body().toString());
                        loginListener.loginFailure(loginFailure.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public static void register(String email, String password, String firstName, String lastName, RegisterationListener registerationListener) {
        FormBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .add("first_name", firstName)
                .add("last_name", lastName)
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
                        registerationListener.registerationSuccessful();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONObject jsonErrorMessage = null;
                    try {
                        jsonErrorMessage = new JSONObject(response.body().string());
                        registerationListener.registerationFailure(jsonErrorMessage.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    public static void profileRetrival(String email, String jwtToken, ProfileRetrivalListener profileRetrivalListener){


        HttpUrl url = HttpUrl.parse("https://node-authenticator-uncc.herokuapp.com/api/auth/profile").newBuilder()
                .addQueryParameter("token",jwtToken)
                .build();
        url = HttpUrl.parse(url + "&email=" + email);
        Request request = new Request.Builder()
                .url(url)
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
                        JSONObject user = res.getJSONObject("data");
                        String email = user.getString("email");
                        String firstname = user.getString("first_name");
                        String lastname = user.getString("last_name");


                        profileRetrivalListener.profileRetrivalSuccessful(new User(firstname,lastname,email));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject profileRetrivalFailure = new JSONObject(response.body().toString());
                        profileRetrivalListener.profileRetrivalFailure(profileRetrivalFailure.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public static void profileUpdate(User user, ProfileUpdateListener profileUpdateListener){

    }
}
