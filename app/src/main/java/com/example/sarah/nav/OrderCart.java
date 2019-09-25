package com.example.sarah.nav;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderCart extends AppCompatActivity {
    //OrderAdapter orderAdapter;
    ArrayList<ModelFood> foodList;
    String email="";
    SessionManager sessionManager;
    RecyclerView recyclerView;
    TextView price;
    int sum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordercart);

        foodList = new ArrayList<>();

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //session mgmt
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(sessionManager.EMAIL);

        recyclerView = findViewById(R.id.rvorder);

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
        price = findViewById(R.id.price);

        RequestQueue requestQueue = Volley.newRequestQueue(OrderCart.this);
        String URL = ""+del+":8080/getOrdered";

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
                if (result != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        Log.d("jsonAray", "" + jsonArray);
                        Log.d("Size of JSON Array", "" + jsonArray.length());
                        int i;
                        //int sum=0;
                        for (i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String restaurant_name = null,category = null, imgname = null, price = null,rid = null, description = null;

                            try {
                                restaurant_name = jsonObject1.getString("restaurant_name");
                                category = jsonObject1.getString("category");
                                imgname = jsonObject1.getString("image");
                                price = jsonObject1.getString("price");
                                rid = jsonObject1.getString("rid");
                                sum = sum + Integer.parseInt(price);
                                Log.d("sum", String.valueOf(sum));
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                            foodList.add(new ModelFood(restaurant_name, category, imgname,price,rid));

                        }
                        Log.d("sum outside loop", String.valueOf(sum));
                        price.setText("Price:₹ "+String.valueOf(sum));
                        //String sum_pass = String.valueOf(sum);

                        final OrderAdapter orderAdapter = new OrderAdapter(OrderCart.this, foodList);
                        recyclerView.setAdapter(orderAdapter);

                        //int finalSum = sum;
                        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
                            public void onItemClick(int position) {
                                ModelFood item = foodList.get(position);
                                int a = sendData(item.getRestaurant_name(), item.getCategory(), item.getImgname(), item.getRid(),item.getPrice(), sum);
                                sum = a;
                                foodList.remove(position);
                                orderAdapter.notifyItemRemoved(position);
                                Toast.makeText(OrderCart.this, "Removed successfully", Toast.LENGTH_SHORT).show();
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


    public int sendData(String restaurant_name, String category, String imgname, String rid,String Price,int sum1){

        RequestQueue requestQueue = Volley.newRequestQueue(OrderCart.this);
        getIp ip = new getIp();
        String del = ip.getIp();
        String URL = ""+del+":8080/delOrdered";


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email",email);
            jsonObject.put("restaurant_name", ""+restaurant_name);
            jsonObject.put("category", ""+category);
            jsonObject.put("rid",""+rid);
            jsonObject.put("image",""+imgname);
            Integer.parseInt(Price);
            Log.d("price","price "+sum1+ " "+Price);
            sum1 = sum1 - Integer.parseInt(Price);
            sum = sum1;
            price.setText("Price:₹ "+String.valueOf(sum1));
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
        return sum1;
    }
    /////////////////////////////////
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}

