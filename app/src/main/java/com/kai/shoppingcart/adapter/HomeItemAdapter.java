package com.kai.shoppingcart.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kai.shoppingcart.R;
import com.kai.shoppingcart.model.Item;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemHolder> {
    List<Item> itemList;
    List<Item> cartItemList;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public HomeItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
        cartItemList = new ArrayList<>();
    }

    public void updateItemList(List<Item> itemList){
        this.itemList.clear();
        this.itemList.addAll(itemList);
    }

    @NonNull
    @Override
    public HomeItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home,parent,false);
        HomeItemHolder holder = new HomeItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemHolder holder, int position) {
        Item item = itemList.get(position);
        holder.textViewName.setText(item.getName());
        holder.textViewPrice.setText("$"+ df.format(item.getPrice()));
        holder.textViewPrice.setPaintFlags(holder.textViewPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.textViewQuantity.setText("0");
        double finalPrice = item.getPrice() - ((item.getDiscount() * item.getPrice())/100);
        holder.textViewFinalPrice.setText("$" + df.format(finalPrice));

        Picasso.get().load(item.getUri()).placeholder(R.mipmap.ic_launcher_round).into(holder.imageViewItem);
        holder.floatingActionButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlusClicked(holder,item);
            }
        });
        holder.floatingActionButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMinusClicked(holder,item);
            }
        });
    }

    private void onPlusClicked(HomeItemHolder holder, Item item) {
        int quantity = Integer.parseInt(holder.textViewQuantity.getText().toString());
        quantity++;
        holder.textViewQuantity.setText(""+quantity);
        item.setQuantity(quantity);
        if(cartItemList.contains(item)){
            cartItemList.set(cartItemList.indexOf(item),item);
        }
        else{
            cartItemList.add(item);
        }
    }
    private void onMinusClicked(HomeItemHolder holder, Item item) {
        int quantity = Integer.parseInt(holder.textViewQuantity.getText().toString());
        if(quantity == 0){
            //can not make quantity negative
        }
        else{
            quantity--;
            holder.textViewQuantity.setText(""+quantity);
            item.setQuantity(quantity);
            if(quantity == 0){
                cartItemList.remove(cartItemList.indexOf(item));
            }
            else{
                cartItemList.set(cartItemList.indexOf(item),item);
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public List<Item> getCartItemList(){
        return cartItemList;
    }
}
class HomeItemHolder extends RecyclerView.ViewHolder{

    TextView textViewName;
    TextView textViewPrice;
    TextView textViewFinalPrice;
    TextView textViewQuantity;
    ImageView imageViewItem;
    FloatingActionButton floatingActionButtonPlus;
    FloatingActionButton floatingActionButtonMinus;

    public HomeItemHolder(@NonNull View itemView) {
        super(itemView);
        textViewName = itemView.findViewById(R.id.textViewName);
        textViewPrice = itemView.findViewById(R.id.textViewPrice);
        textViewFinalPrice = itemView.findViewById(R.id.textViewFinalPrice);
        textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
        imageViewItem = itemView.findViewById(R.id.imageViewItem);
        floatingActionButtonPlus = itemView.findViewById(R.id.floatingActionButtonPlus);
        floatingActionButtonMinus = itemView.findViewById(R.id.floatingActionButtonMinus);
    }
}
