package com.kai.shoppingcart.listener;

import com.kai.shoppingcart.model.Item;

import java.util.List;

public interface ItemRetrivalListener {
    void itemRetrivalSuccessful(List<Item> itemList);
    void itemRetrivalFailure(String message);
}
