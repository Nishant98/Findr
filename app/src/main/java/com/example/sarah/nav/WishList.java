package com.example.sarah.nav;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class WishList extends AppCompatActivity {
    FoodAdapter foodAdapter;
    ArrayList<Data> foodList;
    String email="";
    SessionManager sessionManager;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        foodList = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist);
        foodList = new ArrayList<>();
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(sessionManager.EMAIL);

        recyclerView = findViewById(R.id.rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvlayoutmanager = linearLayoutManager;
        recyclerView.setLayoutManager(rvlayoutmanager);

        getData();
    }
    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    /////////////////////////////////////////////////////////////////////
    public void getData(){
        getIp ip = new getIp();
        String del = ip.getIp();

        RequestQueue requestQueue = Volley.newRequestQueue(WishList.this);
        String URL = ""+del+":8080/getWishlist";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String requestBody = jsonObject.toString();
        Log.d("str", "str is" + requestBody);

        ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                Log.d("result is ", "" + result);
                if (result != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        Log.d("jsonAray", "" + jsonArray);
                        Log.d("Size of JSON Array", "" + jsonArray.length());
                        int i;
                        for (i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String restaurant_name = null,category = null, imgname = null, price = null,rid = null, description = null;
                            try {
                                restaurant_name = jsonObject1.getString("restaurant_name");
                                category = jsonObject1.getString("category");
                                imgname = jsonObject1.getString("image");
                                price = jsonObject1.getString("price");
                                rid = jsonObject1.getString("rid");
                                description = jsonObject1.getString("description");
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }

//                            Log.d("rest name", "" + restaurant_name);
//                            Log.d("cate name", "" + category);
//                            Log.d("image name", "" + imgname);
//                            Log.d("price", "" + price);
//                            Log.d("rid", "" + rid);

                            foodList.add(new Data(restaurant_name, category, imgname,price, description,rid));
                        }

                        Log.d("foodlist", "" + foodList);
                        final FoodAdapter foodAdapter = new FoodAdapter(WishList.this, foodList);
                        recyclerView.setAdapter(foodAdapter);

                        foodAdapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
                            public void onItemClick(int position) {
                                Data item = foodList.get(position);
//                                Log.d("Andar ka maal", ""+item);
//                                Log.d("maal ka restaurant", ""+item.getRestaurant_name());
//                                Log.d("maal ka category", ""+item.getCategory());
//                                Log.d("maal ka Image name", ""+item.getImgname());
//                                Log.d("maal ka price", ""+item.getPrice());

                                sendData(item.getRestaurant_name(), item.getCategory(), item.getImgname(), item.getRid());
                                foodList.remove(position);
                                foodAdapter.notifyItemRemoved(position);
                                Toast.makeText(WishList.this, "Sent to cart Successfully", Toast.LENGTH_SHORT).show();

                            }


                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error: ", "Volley needs attention");
            }

        });
    }
    //end of getUrls/////////////////////////////////////////////////////////////


    public void sendData(String restaurant_name, String category, String imgname, String rid){

        RequestQueue requestQueue = Volley.newRequestQueue(WishList.this);
        getIp ip = new getIp();
        String del = ip.getIp();
        String URL = ""+del+":8080/order";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("restaurant_name", ""+restaurant_name);
            jsonObject.put("category", ""+category);
            jsonObject.put("email",email);
            jsonObject.put("image",""+imgname);
            jsonObject.put("rid",""+rid);

            Log.d("jsonobject", ""+jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        final String requestBody = jsonObject.toString();
        ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                Log.d("Data sent =", "" + result);
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error: ", "Volley needs attention");
            }

        });
    }
/////////////////////////////////
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //Toast.makeText(getApplicationContext(), "Back button is pressed", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}
