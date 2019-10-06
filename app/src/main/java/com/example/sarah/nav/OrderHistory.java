package com.example.sarah.nav;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderHistory extends AppCompatActivity {
    ArrayList<Data> foodList;
    String email="";
    SessionManager sessionManager;
    RecyclerView recyclerView;

    //shimmer
    ProgressBar loading;
    ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        foodList = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        foodList = new ArrayList<>();
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(sessionManager.EMAIL);
        Log.d("email",email);

        recyclerView = findViewById(R.id.review);
        //loading=findViewById(R.id.loading);


        //shimmer
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container_history);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvlayoutmanager = linearLayoutManager;
        recyclerView.setLayoutManager(rvlayoutmanager);

        getData();
    }

    //shimmer
    @Override
    protected void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmer();
    }
    //end shimmer
    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Refresh to Load Items", Toast.LENGTH_SHORT).show();
        return true;
    }

    /////////////////////////////////////////////////////////////////////
    public void getData(){
        getIp ip = new getIp();
        String del = ip.getIp();

        RequestQueue requestQueue = Volley.newRequestQueue(OrderHistory.this);
        String URL = ""+del+":8080/getHistory";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String requestBody = jsonObject.toString();

        ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                Log.d("result is ", "" + result);
                if (result != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        int i;
                        for (i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String restaurant_name = null,category = null, imgname = null, price = null,rid = null;
                            String description = "Lorem ipsum dolor sit amet, proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
                            try {
                                restaurant_name = jsonObject1.getString("restaurant_name");
                                category = jsonObject1.getString("category");
                                imgname = jsonObject1.getString("image");
                                price = jsonObject1.getString("price");
                                rid = jsonObject1.getString("rid");
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                            foodList.add(new Data(restaurant_name, category, imgname,price, description,rid));
                        }

                        Log.d("foodlist", "" + foodList);
//                        final FoodAdapter foodAdapter = new FoodAdapter(OrderHistory.this, foodList);
//                        recyclerView.setAdapter(foodAdapter);

                        final OrderHistoryAdapter historyAdapter = new OrderHistoryAdapter(OrderHistory.this,foodList);
                        recyclerView.setAdapter(historyAdapter);
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}

