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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Data> Mlist;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public OrderHistoryAdapter(Context mContext, ArrayList<Data> mlist) {
        this.mContext = mContext;
        Mlist = mlist;
        Log.d("mList", "Mlist yeh hai "+Mlist);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflator = LayoutInflater.from(mContext);
        View view =  layoutInflator.inflate(R.layout.rvhist, parent ,false);
        ViewHolder viewHolder = new ViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data foodItem  =  Mlist.get(position);
        ImageView image = holder.food_image;

        TextView name, price_order,rest_order, time_view;
        name = holder.food_name;
        rest_order = holder.rest_name;
        price_order = holder.food_price;
        time_view = holder.food_time;

        String restaurant_name, category, del,imgname,rid,time;
        getIp ip = new getIp();
        del = ip.getIp();

        restaurant_name = foodItem.getRestaurant_name();
        category = foodItem.getCategory();
        rid = foodItem.getRid();
        imgname = foodItem.getImgname();
        time = foodItem.getTime();

        //Log.d("restaurant name is",""+restaurant_name);
        //Log.d("category is",""+category);
        //Log.d("image name is",""+imgname);

        String loc = ""+del+":8080/images/"+rid+"/"+category+"/"+imgname;
        //loc  = loc.replace('\\', '/');
        //Log.d("loc",loc);

        Picasso.get().load(""+loc).centerCrop().fit().error(R.drawable.ic_launcher_background).into(image, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("success","LOADED");
            }

            @Override
            public void onError(Exception e) {
                Log.d("error",""+e);
            }
        });

        rest_order.setText(restaurant_name);
        name.setText(category);
        price_order.setText("₹"+foodItem.getPrice());
        time_view.setText(time);
    }


    @Override
    public int getItemCount() {
        return Mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView time;
        ImageView food_image;
        TextView food_name, food_price, rest_name,food_time;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView) ;
            food_image = itemView.findViewById(R.id.food_image_history);
            food_name = itemView.findViewById(R.id.food_name_history);
            food_price = itemView.findViewById(R.id.food_price_history);
            rest_name = itemView.findViewById(R.id.rest_name_history);
            food_time = itemView.findViewById(R.id.food_time);

        }
    }
}
