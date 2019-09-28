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
    private OrderAdapter.OnItemClickListener mListener;


    public interface OnItemClickListener{
        void onItemClick(int position);

    }

    public void setOnItemClickListener(OrderAdapter.OnItemClickListener listener){
        mListener = listener;
    }

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
        ViewHolder viewHolder = new ViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelFood foodItem  =  Mlist_order.get(position);
        Log.d("tag", "jholu    "+foodItem);

        ImageView image = holder.food_image;
        TextView name, price, order_rest_name;

        name = holder.food_name;
        price = holder.food_price;
        order_rest_name = holder.order_rest_name;

        String restaurant_name, category, imgname,rid;

        getIp ip = new getIp();
        String del = ip.getIp();

        rid = foodItem.getRid();
        restaurant_name = foodItem.getRestaurant_name();
        Log.d("restaurant name is",""+restaurant_name);

        category = foodItem.getCategory();
        Log.d("category is",""+category);

        imgname = foodItem.getImgname();
        Log.d("image name is",""+imgname);

        String loc = ""+del+":8080/images/"+rid+"/"+category+"/"+imgname;
        loc  = loc.replace('\\', '/');
        Log.d("loc order",loc);

        Picasso.get().load(""+loc).centerCrop().fit().error(R.drawable.ic_launcher_background).into(image);
        name.setText(category);
        order_rest_name.setText(restaurant_name);
        price.setText("₹"+foodItem.getPrice());

    }

    @Override
    public int getItemCount() {
        return Mlist_order.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView food_image;
        TextView food_name, food_price,order_rest_name;
        Button food_order,trash;
        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView) ;
            food_image = itemView.findViewById(R.id.food_order_image);
            food_name = itemView.findViewById(R.id.food_order_name);
            food_price = itemView.findViewById(R.id.food_order_price);
            food_order=itemView.findViewById(R.id.food_order);
            order_rest_name = itemView.findViewById(R.id.order_rest_name);
            trash = itemView.findViewById(R.id.trash);

            trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("On cart click", "Trash Clicked");
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }

            });
        }
    }
}