package com.kai.shoppingcart.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.kai.shoppingcart.model.User;
import com.kai.shoppingcart.listener.ProfileRetrivalListener;
import com.kai.shoppingcart.listener.ProfileUpdateListener;
import com.kai.shoppingcart.util.APIHelper;

public class ProfileViewModel extends AndroidViewModel implements ProfileRetrivalListener, ProfileUpdateListener {

    MutableLiveData<String> messageLiveData;
    MutableLiveData<User> profileLiveData;
    MutableLiveData<Boolean> profileUpdateLiveData;
    public ProfileViewModel(@NonNull Application application) {
        super(application);
        messageLiveData = new MutableLiveData<>();
        profileLiveData = new MutableLiveData<>();
        profileUpdateLiveData = new MutableLiveData<>();
    }

    public void profileRetrival(String email, String jwtToken){
        APIHelper.profileRetrival(email, jwtToken,this);
    }
    public void profileUpdate(User user){
        APIHelper.profileUpdate(user,this);
    }

    public MutableLiveData<User> getProfileLiveData(){
        return profileLiveData;
    }

    public MutableLiveData<Boolean> getProfileUpdateLiveData(){
        return profileUpdateLiveData;
    }

    public MutableLiveData<String> getMessageLiveData(){
        return messageLiveData;
    }
    @Override
    public void profileRetrivalSuccessful(User user) {
        profileLiveData.postValue(user);
    }

    @Override
    public void profileRetrivalFailure(String message) {
        messageLiveData.postValue(message);
    }

    @Override
    public void profileUpdateSuccessful() {
        profileUpdateLiveData.postValue(true);
    }

    @Override
    public void profileUpdateFailure(String message) {
        messageLiveData.postValue(message);
    }
}
