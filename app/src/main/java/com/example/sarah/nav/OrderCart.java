package com.example.sarah.nav;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class OrderCart extends AppCompatActivity {
    OrderAdapter orderAdapter;
    ArrayList<ModelFood> foodList;
    RecyclerView recyclerView1;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        foodList = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordercart);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        email=bundle.getString("params");
        Log.d("prams",email);

        //Adding back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView1 = findViewById(R.id.rvorder);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvlayoutmanager = linearLayoutManager;
        recyclerView1.setLayoutManager(rvlayoutmanager);

        getData();
    }

    ///Adding back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void getData(){

//        final ArrayList<ModelFood> foodList = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(OrderCart.this);

        getIp ip = new getIp();
        String del = ip.getIp();
        String URL = ""+del+":8080/OrdergetLoc";

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
                Log.d("result order =", "" + result);

               result = result.replaceAll("\'", "");
                if (result != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        Log.d("jsonAray", "" + jsonArray);
                        Log.d("Jsonarray ka  size", "" + jsonArray.length());
                        int i;
                        for (i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String loc = null;
                            try {
                                loc = jsonObject1.getString("location");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String name = null;
                            try {
                                name = jsonObject1.getString("name");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String price = null;
                            try {
                                price = jsonObject1.getString("price");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("loc", "" + loc);
                            Log.d("name", "" + name);
                            Log.d("price", "" + price);
                            foodList.add(new ModelFood(loc, name, price));

                        }

                        Log.d("foodlist", ""+foodList);
                        OrderAdapter orderAdapter = new OrderAdapter(OrderCart.this, foodList);
                        recyclerView1.setAdapter(orderAdapter);
//                        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
//                            public void onItemClick(int position) {
//
//                                Toast.makeText(OrderCart.this, "Sent to cart Successfully", Toast.LENGTH_SHORT).show();
//
//                            }
//
//
//                        });

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("error: ", "hagg diya");
            }

        });
    }

}


