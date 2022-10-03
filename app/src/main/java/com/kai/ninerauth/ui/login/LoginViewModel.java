package com.kai.ninerauth.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.kai.ninerauth.Model.LoginData;
import com.kai.ninerauth.listener.LoginListener;
import com.kai.ninerauth.util.APIHelper;

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
    public void loginSuccessfull(String email, String jwtToken) {
        loginLiveData.postValue(new LoginData(email,jwtToken));
    }

    @Override
    public void loginFailure(String message) {
        //Show failure message
        messageLiveData.postValue(message);
    }
}
