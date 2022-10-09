package com.kai.shoppingcart.ui.cart;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kai.shoppingcart.listener.TokenGetListener;
import com.kai.shoppingcart.listener.TransactionListener;
import com.kai.shoppingcart.util.APIHelper;

public class CartViewModel extends AndroidViewModel implements TokenGetListener, TransactionListener {

    MutableLiveData<String> messageMutableLiveData;
    MutableLiveData<String> tokenMutableLiveData;
    MutableLiveData<Boolean> transactionMutableLiveData;

    public CartViewModel(@NonNull Application application) {
        super(application);
        messageMutableLiveData = new MutableLiveData<>();
        tokenMutableLiveData = new MutableLiveData<>();
        transactionMutableLiveData = new MutableLiveData<>();
    }

    public void getToken(String jwtToken, String customerId){
        APIHelper.getToken(jwtToken, customerId, this);
    }

    public void transaction(String token, String nonceFromTheClient, String deviceDataFromTheClient, String amount){
        APIHelper.transaction(token,nonceFromTheClient,deviceDataFromTheClient,amount,this);
    }
    public MutableLiveData<String> getMessageMutableLiveData(){
        return messageMutableLiveData;
    }

    public MutableLiveData<Boolean> getTransactionMutableLiveData(){
        return transactionMutableLiveData;
    }

    @Override
    public void tokenGetSuccessfull(String brainTreeToken) {
        tokenMutableLiveData.postValue(brainTreeToken);
    }

    @Override
    public void tokenGetFailure(String message) {
        messageMutableLiveData.postValue(message);
    }

    @Override
    public void transactionSuccessfull() {
        transactionMutableLiveData.postValue(true);
    }

    @Override
    public void transactionFailure(String message) {
        messageMutableLiveData.postValue(message);
    }
}