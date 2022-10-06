package com.kai.shoppingcart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kai.shoppingcart.R;
import com.kai.shoppingcart.model.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemHolder> {
    List<Item> itemList;

    public HomeItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
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
        holder.textViewPrice.setText(""+item.getPrice());
        holder.textViewQuantity.setText("0");
        double finalPrice = item.getPrice() - ((item.getDiscount() * item.getPrice())/100);
        holder.textViewFinalPrice.setText("" + finalPrice);

        Picasso.get().load(item.getUri()).placeholder(R.mipmap.ic_launcher_round).into(holder.imageViewItem);
        holder.floatingActionButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlusClicked(holder);
            }
        });
        holder.floatingActionButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMinusClicked(holder);
            }
        });
    }

    private void onPlusClicked(HomeItemHolder holder) {
        int quantity = Integer.parseInt(holder.textViewQuantity.getText().toString());
        quantity++;
        holder.textViewQuantity.setText(""+quantity);
    }
    private void onMinusClicked(HomeItemHolder holder) {
        int quantity = Integer.parseInt(holder.textViewQuantity.getText().toString());
        if(quantity == 0){
//            Toast.makeText(, "", Toast.LENGTH_SHORT).show();
            //can not make quantity negative
        }
        else{
            quantity--;
            holder.textViewQuantity.setText(""+quantity);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
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
        textViewPrice = itemView.findViewById(R.id.textViewName);
        textViewFinalPrice = itemView.findViewById(R.id.textViewFinalPrice);
        textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
        imageViewItem = itemView.findViewById(R.id.imageViewItem);
        floatingActionButtonPlus = itemView.findViewById(R.id.floatingActionButtonPlus);
        floatingActionButtonMinus = itemView.findViewById(R.id.floatingActionButtonMinus);
    }
}
