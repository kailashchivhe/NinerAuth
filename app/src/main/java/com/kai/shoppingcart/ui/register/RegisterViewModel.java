package com.kai.shoppingcart.ui.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.kai.shoppingcart.listener.RegisterationListener;
import com.kai.shoppingcart.util.APIHelper;

public class RegisterViewModel extends AndroidViewModel implements RegisterationListener {

    MutableLiveData<Boolean> registerLiveData;
    MutableLiveData<String> messageLiveData;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        registerLiveData = new MutableLiveData<>();
        messageLiveData = new MutableLiveData<>();
    }

    public void registeration(String email, String password, String firstName, String lastName){
        APIHelper.register(email,password,firstName,lastName,this);
    }
    public MutableLiveData<Boolean> getRegisterLiveData(){
        return registerLiveData;
    }
    public MutableLiveData<String> getMessageLiveData(){
        return messageLiveData;
    }

    @Override
    public void registerationSuccessful() {
        registerLiveData.postValue(true);
    }

    @Override
    public void registerationFailure(String message) {
        //Show failure message
        messageLiveData.postValue(message);
    }
}
