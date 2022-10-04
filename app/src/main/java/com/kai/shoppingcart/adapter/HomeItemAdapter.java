package com.kai.shoppingcart.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemHolder> {
    @NonNull
    @Override
    public HomeItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
class HomeItemHolder extends RecyclerView.ViewHolder{

    public HomeItemHolder(@NonNull View itemView) {
        super(itemView);
    }
}
