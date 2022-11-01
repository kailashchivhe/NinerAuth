package com.kai.shoppingcart.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kai.shoppingcart.listener.ItemRetrivalListener;
import com.kai.shoppingcart.model.Item;
import com.kai.shoppingcart.util.APIHelper;

import java.util.List;

public class HomeViewModel extends AndroidViewModel implements ItemRetrivalListener {

    MutableLiveData<List<Item>> itemListMutableLiveData;
    MutableLiveData<String> messageMutableLiveData;
    public HomeViewModel(@NonNull Application application) {
        super(application);
        itemListMutableLiveData = new MutableLiveData<>();
        messageMutableLiveData = new MutableLiveData<>();
    }

    void getItems(String jwtToken, String region){
        APIHelper.itemRetrival(jwtToken, region,this);
    }

    MutableLiveData<String> getMessageMutableLiveData(){
        return messageMutableLiveData;
    }

    MutableLiveData<List<Item>> getItemsMutableLiveData(){
        return itemListMutableLiveData;
    }
    @Override
    public void itemRetrivalSuccessful(List<Item> itemList) {
        itemListMutableLiveData.postValue(itemList);
    }

    @Override
    public void itemRetrivalFailure(String message) {
        messageMutableLiveData.postValue(message);
    }
}