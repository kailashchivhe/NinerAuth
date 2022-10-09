package com.kai.shoppingcart.util;

import androidx.annotation.NonNull;

import com.kai.shoppingcart.listener.ItemRetrivalListener;
import com.kai.shoppingcart.listener.LoginListener;
import com.kai.shoppingcart.listener.RegisterationListener;
import com.kai.shoppingcart.listener.ProfileRetrivalListener;
import com.kai.shoppingcart.listener.ProfileUpdateListener;
import com.kai.shoppingcart.listener.TokenGetListener;
import com.kai.shoppingcart.listener.TransactionListener;
import com.kai.shoppingcart.model.Item;
import com.kai.shoppingcart.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                        String id = res.getString("id");
                        String jwtToken = res.getString("token");
                        String customerId = res.getString("customerId");

                        loginListener.loginSuccessfull(id, jwtToken, customerId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject loginFailure = new JSONObject(response.body().string());
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

    public static void profileRetrival(String id, String jwtToken, ProfileRetrivalListener profileRetrivalListener){


        HttpUrl url = HttpUrl.parse("https://node-authenticator-uncc.herokuapp.com/api/auth/profile").newBuilder()
                .addQueryParameter("token",jwtToken)
                .addQueryParameter("userId",id)
                .build();
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
                        String firstname = user.getString("firstName");
                        String lastname = user.getString("lastName");

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

        FormBody formBody = new FormBody.Builder()
                .add("id", user.getId())
                .add("firstName", user.getFirstName())
                .add("lastName", user.getLastName())
                .build();

        HttpUrl url = HttpUrl.parse("https://node-authenticator-uncc.herokuapp.com/api/auth/profile").newBuilder()
                .addQueryParameter("token",user.getJwtToken())
                .build();

        Request request = new Request.Builder()
                .url(url)
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
                    profileUpdateListener.profileUpdateSuccessful();
                } else {
                    try {
                        JSONObject updateFailure = new JSONObject(response.body().string());
                        profileUpdateListener.profileUpdateFailure(updateFailure.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public static void itemRetrival(String jwtToken, ItemRetrivalListener itemRetrivalListener){
        HttpUrl url = HttpUrl.parse("https://node-authenticator-uncc.herokuapp.com/api/auth/items").newBuilder()
                .addQueryParameter("token",jwtToken)
                .build();
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
                        JSONArray items = res.getJSONArray("items");
                        List<Item> itemList = new ArrayList<>();
                        for(int i=0;i< items.length();i++){
                            JSONObject item = items.getJSONObject(i);
                            String id = item.getString("_id");
                            String name = item.getString("name");
                            String uri = item.getString("photo");
                            String region = item.getString("region");
                            int discount = item.getInt("discount");
                            double price = item.getDouble("price");
                            itemList.add(new Item(id,name,price,discount,uri,region));
                        }
                        itemRetrivalListener.itemRetrivalSuccessful(itemList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject profileRetrivalFailure = new JSONObject(response.body().string());
                        itemRetrivalListener.itemRetrivalFailure(profileRetrivalFailure.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void getToken(String jwtToken, String customerId, TokenGetListener tokenGetListener){
        HttpUrl url = HttpUrl.parse("https://node-authenticator-uncc.herokuapp.com/api/auth/token").newBuilder()
                .addQueryParameter("token",jwtToken)
                .addQueryParameter("customerId",customerId)
                .build();
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
                        String brainTreeToken = res.getString("token");
                        tokenGetListener.tokenGetSuccessfull(brainTreeToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject profileRetrivalFailure = new JSONObject(response.body().string());
                        tokenGetListener.tokenGetFailure(profileRetrivalFailure.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void transaction(String token, String nonceFromTheClient, String amount, TransactionListener transactionListener){
        FormBody formBody = new FormBody.Builder()
                .add("amount", amount)
                .add("nonceFromTheClient", nonceFromTheClient)
                .build();

        HttpUrl url = HttpUrl.parse("https://node-authenticator-uncc.herokuapp.com/api/auth/transaction").newBuilder()
                .addQueryParameter("token",token)
                .build();

        Request request = new Request.Builder()
                .url(url)
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
                    transactionListener.transactionSuccessfull();
                } else {
                    try {
                        JSONObject updateFailure = new JSONObject(response.body().string());
                        transactionListener.transactionFailure(updateFailure.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
