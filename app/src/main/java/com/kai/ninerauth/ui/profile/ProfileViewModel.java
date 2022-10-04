package com.kai.ninerauth.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.kai.ninerauth.Model.User;
import com.kai.ninerauth.listener.ProfileRetrivalListener;
import com.kai.ninerauth.listener.ProfileUpdateListener;
import com.kai.ninerauth.util.APIHelper;

public class ProfileViewModel extends AndroidViewModel implements ProfileRetrivalListener, ProfileUpdateListener {

    MutableLiveData<String> messageLiveData;
    MutableLiveData<User> profileLiveData;
    public ProfileViewModel(@NonNull Application application) {
        super(application);
        messageLiveData = new MutableLiveData<>();
        profileLiveData = new MutableLiveData<>();
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

    }

    @Override
    public void profileUpdateFailure(String message) {
        messageLiveData.postValue(message);
    }
}
