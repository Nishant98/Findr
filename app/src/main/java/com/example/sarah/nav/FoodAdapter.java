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

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ModelFood> Mlist;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
    void onItemClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public FoodAdapter(Context mContext, ArrayList<ModelFood> mlist) {
        this.mContext = mContext;
        Mlist = mlist;
        Log.d("mList", ""+Mlist);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflator = LayoutInflater.from(mContext);
       View view =  layoutInflator.inflate(R.layout.rv_food_items, parent ,false);
        ViewHolder viewHolder = new ViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelFood foodItem  =  Mlist.get(position);
        Log.d("tag", "the food item is    "+foodItem);
        ImageView image = holder.food_image;
        TextView name, price;

        name = holder.food_name;
        price = holder.food_price;

        String index1, index2, del;
        getIp ip = new getIp();
        del = ip.getIp();
        index1 = foodItem.getIndex1();
        Log.d("index 1 is",""+index1);
        String loc = ""+del+"/Findr"+index1;
        loc  = loc.replace('\\', '/');

        Log.d("loc",loc);
        //Glide.with(mContext).load(""+loc).error(R.drawable.shopping_cart).into(image);
        //Glide.with(mContext).load(R.drawable.ic_launcher_background).error(R.drawable.shopping_cart).into(image);

        Picasso.get().load(""+loc).centerCrop().fit().error(R.drawable.ic_launcher_background).into(image, new Callback() {

        //Picasso.get().load("http://cdn.journaldev.com/wp-content/uploads/2016/11/android-image-picker-project-structure.png").centerCrop().fit().error(R.drawable.ic_launcher_background).into(image, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("success","LOADED");
            }

            @Override
            public void onError(Exception e) {
                Log.d("error",""+e);

            }
        });
        name.setText(foodItem.getIndex2());
        price.setText("₹"+foodItem.getPrice());

    }


    @Override
    public int getItemCount() {
        return Mlist.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView food_image;
        TextView food_name, food_price;
        Button food_order;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView) ;
            food_image = itemView.findViewById(R.id.food_image);
            food_name = itemView.findViewById(R.id.food_name);
            food_price = itemView.findViewById(R.id.food_price);
            food_order =  itemView.findViewById(R.id.food_order);

            food_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("On cart click", "Cart Clicked");
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