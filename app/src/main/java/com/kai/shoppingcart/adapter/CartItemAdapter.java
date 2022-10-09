package com.kai.shoppingcart.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kai.shoppingcart.R;
import com.kai.shoppingcart.model.Item;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemHolder>{

    List<Item> list;
    String TAG="Vidit";

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public CartItemAdapter(List<Item> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CartItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
        CartItemHolder holder = new CartItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemHolder holder, int position) {
        Item item = list.get(position);
        holder.textViewCartName.setText(item.getName());
        double finalPrice = item.getPrice() - ((item.getDiscount() * item.getPrice())/100);
        holder.textViewCartPrice.setText("Discounted Price $: " + df.format(finalPrice));
        holder.textViewCartFinalPrice.setText("Total Price $: "+ df.format(finalPrice * item.getQuantity()));
        holder.textViewCartQuantity.setText("Quantity: "+item.getQuantity());

        try{
            Picasso.get()
                    .load(item.getUri())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(holder.imageViewCartItem);
        }
        catch (Exception e){
            Log.d(TAG, "onBindViewHolder: " + e.getMessage());
        }


        holder.buttonCartRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), list.size());
                holder.itemView.setVisibility(View.GONE);
                Log.d("Vidit", "onClick: Size: " + list.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
class CartItemHolder extends RecyclerView.ViewHolder{

    TextView textViewCartName;
    TextView textViewCartPrice;
    TextView textViewCartFinalPrice;
    TextView textViewCartQuantity;
    ImageView imageViewCartItem;
    Button buttonCartRemove;

    public CartItemHolder(@NonNull View itemView) {
        super(itemView);
        textViewCartPrice = itemView.findViewById(R.id.textViewCartPrice);
        textViewCartFinalPrice = itemView.findViewById(R.id.textViewCartFinalPrice);
        textViewCartName = itemView.findViewById(R.id.textViewCartName);
        textViewCartQuantity = itemView.findViewById(R.id.textViewCartQuantity);
        imageViewCartItem = itemView.findViewById(R.id.imageViewCartItem);
        buttonCartRemove = itemView.findViewById(R.id.buttonCartRemove);
    }
}
