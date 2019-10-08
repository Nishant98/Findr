//package com.example.sarah.nav;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.android.volley.RequestQueue;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class OrderCart extends AppCompatActivity {
//    OrderAdapter orderAdapter;
//    ArrayList<ModelFood> foodList;
//    RecyclerView recyclerView1;
//    Button trash;
//    SessionManager sessionManager;
//    String email="";
//    ArrayList<ModelFood> a;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        //foodList = new ArrayList<>();
//        a = new ArrayList<>();
//
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.ordercart);
//
//        trash = findViewById(R.id.trash);
//        trash.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final OrderAdapter orderAdapter = new OrderAdapter(OrderCart.this, foodList);
//                Toast.makeText(getApplicationContext(),"DABA",Toast.LENGTH_SHORT).show();
//
//                //del(restaurant_name,category,imgname,rid);
//                }
//        });
//
//        sessionManager = new SessionManager(getApplicationContext());
//        sessionManager.checkLogin();
//        HashMap<String, String> user = sessionManager.getUserDetails();
//        email = user.get(sessionManager.EMAIL);
//
//        recyclerView1 = findViewById(R.id.rvorder);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        RecyclerView.LayoutManager rvlayoutmanager = linearLayoutManager;
//        recyclerView1.setLayoutManager(rvlayoutmanager);
//
//        getData();
//    }
//
//
//
//    public void getData(){
//        RequestQueue requestQueue = Volley.newRequestQueue(OrderCart.this);
//
//        getIp ip = new getIp();
//        String del = ip.getIp();
//        String URL = ""+del+":8080/getOrdered";
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("email", email);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        final String requestBody = jsonObject.toString();
//        Log.d("str", "str is" + requestBody);
//
//        ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback() {
//            @Override
//            public void onSuccessResponse(String result) {
//                Log.d("result order =", "" + result);
//
//               //result = result.replaceAll("\'", "");
//                if (result != null) {
//                    try {
//                        JSONArray jsonArray = new JSONArray(result);
//                        Log.d("jsonAray", "" + jsonArray);
//                        Log.d("Jsonarray ka  size", "" + jsonArray.length());
//                        int i;
//                        for (i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//
//                            String restaurant_name = null, category = null, imgname = null, price  = null, rid = null;
//                            try {
//                                restaurant_name = jsonObject1.getString("restaurant_name");
//                                category = jsonObject1.getString("category");
//                                imgname = jsonObject1.getString("image");
//                                price = jsonObject1.getString("price");
//                                rid = jsonObject1.getString("rid");
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                            Log.d("rest name", "" + restaurant_name);
//                            Log.d("imgname", "" + imgname);
//                            Log.d("price", "" + price);
//                            Log.d("category", ""+category);
//                            Log.d("rid", ""+rid);
//
//                            foodList.add(new ModelFood(restaurant_name, category, imgname, price,rid));
//                        }
//
//                        Log.d("foodlist", ""+foodList);
//                        final OrderAdapter orderAdapter = new OrderAdapter(OrderCart.this, a);
//
//                        recyclerView1.setAdapter(orderAdapter);
//
//                        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
//                            public void onItemClick(int position) {
//                                ModelFood item = a.get(position);
//                                del(item.getRestaurant_name(), item.getCategory(), item.getImgname(), item.getRid());
//                                a.remove(position);
//                                orderAdapter.notifyItemRemoved(position);
//                                Toast.makeText(OrderCart.this, "Sent to cart Successfully", Toast.LENGTH_SHORT).show();
//
//                            }
//
//
//                        });
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Log.d("error: ", "hagg diya");
//            }
//
//        });
//    }
//
//
//    public void del(String restaurant_name,String category, String imgname, String rid){
//        RequestQueue requestQueue = Volley.newRequestQueue(OrderCart.this);
//        getIp ip = new getIp();
//        String del = ip.getIp();
//        String URL = ""+del+":8080/delOrdered";
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("email", email);
//            jsonObject.put("restaurant name",restaurant_name);
//            jsonObject.put("category",category);
//            jsonObject.put("image",imgname);
//            jsonObject.put("rid",rid);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        final String requestBody = jsonObject.toString();
//        Log.d("str", "str is" + requestBody);
//        ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback() {
//            @Override
//            public void onSuccessResponse(String result) {
//                if (result.equals("Removed")) {
//                    try {
//                        JSONArray jsonArray = new JSONArray(result);
//                        Toast.makeText(getApplicationContext(),"Removed item",Toast.LENGTH_SHORT).show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Log.d("error: ", "hagg diya");
//            }
//
//        });
//
//
//    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        Toast.makeText(getApplicationContext(), "Back button is pressed", Toast.LENGTH_SHORT).show();
//        startActivity(intent);
//    }
//
//
package com.example.sarah.nav;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import static com.example.sarah.nav.AppNotification.CHANNEL_1_ID;

public class OrderCart extends AppCompatActivity implements ExampleDialog.ExampleDialogListener{
    //for notifications
    //private NotificationManagerCompat notificationManager;


    ArrayList<ModelFood> foodList;
    String email="";
    SessionManager sessionManager;
    RecyclerView recyclerView;
    TextView price;
    Button order_button;
    int sum = 0;
    int flag=0;
    ShimmerFrameLayout mShimmerViewContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordercart);

        foodList = new ArrayList<>();
        recyclerView = findViewById(R.id.rvorder);

        //for notification
        //notificationManager = NotificationManagerCompat.from(this);


        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //session mgmt
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(sessionManager.EMAIL);

        //shimmer
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvlayoutmanager = linearLayoutManager;
        recyclerView.setLayoutManager(rvlayoutmanager);
        getData();
        order_button = findViewById(R.id.food_order);
        price = findViewById(R.id.price);
//        if(foodList.isEmpty()){
//
//            //Toast.makeText(this, "Order Cart Empty", Toast.LENGTH_LONG).show();
//            setContentView(R.layout.error_page);
//
//        }
//        else{
//
//            order_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //Toast.makeText(getApplicationContext(), "Clicked on Order", Toast.LENGTH_SHORT).show();
//                    openDialog();
//                    removeOrder();
//                    //sendData();
//                    //sendOnChannel1();
//
//                }
//            });
//
//        }

        order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Clicked on Order", Toast.LENGTH_SHORT).show();
                openDialog();
                ExampleDialog obj1 = new ExampleDialog();
                Log.d("example dialgo", "example dialog  "+obj1);
                int a =obj1.flag2();
                Log.d("a","a is "+a);
                if(a!=0){
                    removeOrder();
                }
                //sendData();
                //sendOnChannel1();

            }
        });




    }
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

    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }
    //For Notification
//    public void sendOnChannel1() {
//        String title = "Findr";
//        String message = "Order Placed";
//        Log.d("enter notif",""+title+" "+message);
//
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
//                .setSmallIcon(R.drawable.notifications)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .build();
//        notificationManager.notify(1, notification);
//    }
    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        //Toast.makeText(this, "Refresh to Load Items", Toast.LENGTH_SHORT).show();
        return true;
    }


    public void error(){
        Log.d("flag","flag is "+flag);
        if(flag==1) {
            if (foodList.isEmpty()) {
                setContentView(R.layout.error_page);
            }
            else {
                order_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "Clicked on Order", Toast.LENGTH_SHORT).show();
                        openDialog();
                        ExampleDialog obj1 = new ExampleDialog();
                        Log.d("example dialgo", "example dialog  "+obj1);
                        int a =obj1.flag2();
                        Log.d("a","a is "+a);
                        if(a!=0){
                        removeOrder();
                        }
                        //sendData();
                        //sendOnChannel1();

                    }
                });
            }
        }
        else{
            if (foodList.isEmpty()) {
                setContentView(R.layout.error_page);
            }
            setContentView(R.layout.error_page);
            //recreate();
        }


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
                if(result.equals("not found")){
                    //Toast.makeText(OrderCart.this, "You've not ordered anything. :(", Toast.LENGTH_SHORT).show();
                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    flag = 0;
                    error();
                }
                else if (result != null) {
                    flag=1;

                    try {
                        JSONArray jsonArray = new JSONArray(result);
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

                        final OrderAdapter orderAdapter = new OrderAdapter(OrderCart.this, foodList);
                        recyclerView.setAdapter(orderAdapter);
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
                            public void onItemClick(int position) {
                                ModelFood item = foodList.get(position);
                                int a = sendData(item.getRestaurant_name(), item.getCategory(), item.getImgname(), item.getRid(),item.getPrice(), sum);
                                sum = a;
                                foodList.remove(position);

                                if(foodList.isEmpty()){
                                    flag = 0;
                                }
                                error();
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

    //error();
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



    public void removeOrder(){
        getIp ip = new getIp();
        String del = ip.getIp();
        RequestQueue requestQueue = Volley.newRequestQueue(OrderCart.this);
        String URL = ""+del+":8080/removeOrder";
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
                if (result.equals("Remove Ordered List")) {
                    //foodList.clear();
                    Log.d("result",result);
                    //foodList.removeAll();

                }
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
        startActivity(intent);
    }

    @Override
    public void applyTexts(String username, String password) {
        Toast.makeText(this, "apply texts", Toast.LENGTH_SHORT).show();
    }
}

