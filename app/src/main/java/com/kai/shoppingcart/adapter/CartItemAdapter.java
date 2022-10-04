package com.kai.shoppingcart.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemHolder>{
    @NonNull
    @Override
    public CartItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
class CartItemHolder extends RecyclerView.ViewHolder{

    public CartItemHolder(@NonNull View itemView) {
        super(itemView);
    }
}
