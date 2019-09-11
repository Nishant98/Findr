package com.example.sarah.nav;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ModelFood> Mlist_order;

    public OrderAdapter(Context mContext, ArrayList<ModelFood> mlistOrder) {
        this.mContext = mContext;
        Mlist_order = mlistOrder;
        Log.d("mList", ""+ Mlist_order);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflator = LayoutInflater.from(mContext);
        View view =  layoutInflator.inflate(R.layout.rv_order, parent ,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelFood foodItem  =  Mlist_order.get(position);
        Log.d("tag", "jholu    "+foodItem);
        ImageView image = holder.food_image;
        TextView name, price;
        name = holder.food_name;
        price = holder.food_price;
        String index1, index2;
        index1 = foodItem.getIndex1();
        getIp ip = new getIp();
        String del = ip.getIp();
        String loc = ""+del+"/Findr"+index1;
        Log.d("hi leeaa", "fooditem.getimage    "+loc);
        Picasso.get().load(""+loc).centerCrop().fit().error(R.drawable.ic_launcher_background).into(image);
        name.setText(foodItem.getIndex2());
        price.setText("₹"+foodItem.getPrice());

    }

    @Override
    public int getItemCount() {
        return Mlist_order.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView food_image;
        TextView food_name, food_price;
        Button food_order;
        public ViewHolder(@NonNull View itemView) {
            super(itemView) ;
            food_image = itemView.findViewById(R.id.food_order_image);
            food_name = itemView.findViewById(R.id.food_order_name);
            food_price = itemView.findViewById(R.id.food_order_price);
            food_order=itemView.findViewById(R.id.food_order);
        }
    }
}