package com.kai.shoppingcart.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.kai.shoppingcart.model.LoginData;
import com.kai.shoppingcart.listener.LoginListener;
import com.kai.shoppingcart.util.APIHelper;

public class LoginViewModel extends AndroidViewModel implements LoginListener {

    MutableLiveData<LoginData> loginLiveData;
    MutableLiveData<String> messageLiveData;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginLiveData = new MutableLiveData<>();
        messageLiveData = new MutableLiveData<>();
    }
    public void login(String email, String password){
        APIHelper.login(email, password, this);
    }

    public MutableLiveData<LoginData> getLoginLiveData(){
        return loginLiveData;
    }
    public MutableLiveData<String> getMessageLiveData(){
        return messageLiveData;
    }

    @Override
    public void loginSuccessfull(String id, String jwtToken) {
        loginLiveData.postValue(new LoginData(id,jwtToken));
    }

    @Override
    public void loginFailure(String message) {
        //Show failure message
        messageLiveData.postValue(message);
    }
}
