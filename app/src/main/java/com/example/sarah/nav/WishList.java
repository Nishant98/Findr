package com.example.sarah.nav;

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

public class WishList extends AppCompatActivity {
    FoodAdapter foodAdapter;
    ArrayList<ModelFood> foodList;
    String email;
    SharedPreferences pref;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        foodList = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist);

        pref = getSharedPreferences("email",MODE_PRIVATE);
        //result.setText("Hello, "+prf.getString("username",null));
        Toast.makeText(WishList.this,"Hello "+pref,Toast.LENGTH_SHORT).show();

        //rec email from main activity
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        email=bundle.getString("params");
        Log.d("prams",email);


        //Adding back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvlayoutmanager = linearLayoutManager;
        recyclerView.setLayoutManager(rvlayoutmanager);

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
/////////////////////////////
    public void getData(){
        getIp ip = new getIp();
        String del = ip.getIp();
        RequestQueue requestQueue = Volley.newRequestQueue(WishList.this);
        String URL = ""+del+":8080/getLoc";

        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject.put("test", "null");
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

                result = result.replaceAll("\'", "");


                if (result != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        Log.d("jsonAray", "" + jsonArray);
                        Log.d("Size of JSON Array", "" + jsonArray.length());
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

                        Log.d("foodlist", "" + foodList);
                        final FoodAdapter foodAdapter = new FoodAdapter(WishList.this, foodList);
                        recyclerView.setAdapter(foodAdapter);

                        foodAdapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
                            public void onItemClick(int position) {
                                ModelFood item = foodList.get(position);
                                Log.d("Andar ka maal", ""+item);
                                Log.d("maal ka index 1", ""+item.getIndex1());
                                Log.d("maal ka index 2", ""+item.getIndex2());
                                Log.d("maal ka price", ""+item.getPrice());
                                sendData(item.getIndex1(), item.getIndex2(), item.getPrice());
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

                Log.d("error: ", "hagg diya");
            }

        });
    }
    public void sendData(String index1, String index2, String price){

        RequestQueue requestQueue = Volley.newRequestQueue(WishList.this);


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("index2", ""+index1);
            jsonObject.put("name", ""+index2);
            //jsonObject.put("price", ""+price);
            jsonObject.put("email","leeaanair@gmail.com");
            Log.d("jsonobject", ""+jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        final String requestBody = jsonObject.toString();
        Log.d("str", "str is" + requestBody);
        getIp ip = new getIp();
        String del = ip.getIp();
        Log.d("ip",del);
        String URL = ""+del+":8080/sendData";
        ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                Log.d("data hua send =", "" + result);
            }
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("error: ", "data send main problem");
            }

        });
    }
}
