package com.example.sarah.nav;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment {
    private MyAppAdapter myAppAdapter;
    ArrayList<Data> array;
    SwipeFlingAdapterView flingContainer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        array = new ArrayList<>();
        getIp ip = new getIp();
        String del = ip.getIp();

        //array.add(new Data("http://192.168.0.103:8080/images/nonveg/1082564.jpg", "1", "64", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
        array.add(new Data("Guddu ka Dhaaba", "apple pie", "407267.jpg", "50", "Lorem ipsum dolor sit amet, consectetur adipiscing elit,","1"));

        getUrls();
        Log.d("new", "initial " + array);
        flingContainer = (SwipeFlingAdapterView) getActivity().findViewById(R.id.frame);


        final SwipeRefreshLayout refresh = (SwipeRefreshLayout) getActivity().findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), "REEEEE", Toast.LENGTH_SHORT).show();
                       myAppAdapter.notifyDataSetChanged();
                        refresh.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                //myAppAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Data a=array.remove(0);
                Log.d("array remove", String.valueOf(a));
                myAppAdapter.notifyDataSetChanged();
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                Data a=array.remove(0);
                Log.d("array remove", String.valueOf(a));

                String restaurant_name=a.getRestaurant_name();
                String category = a.getCategory();
                String imgname = a.getImgname();
                String rid = a.getRid();
                //String desc = a.getDescription();

                Log.d("right card rest name",restaurant_name);
                Log.d("right card category ",category);
                Log.d("right card img ",imgname);
                Log.d("right card rid ",rid);
                //Log.d("right card desc",desc);





                rightSwipe(restaurant_name,category,imgname,rid);

                myAppAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity().getApplicationContext(),"RiGHT",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                getUrls();
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
                myAppAdapter.notifyDataSetChanged();
            }
        });
    }

    public static class ViewHolder {
        public FrameLayout background;
        TextView DataText;
        public ImageView cardImage;
        }

    public class MyAppAdapter extends BaseAdapter {


        List<Data> parkingList;
        public Context context;

        private MyAppAdapter(List<Data> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;


            ViewHolder viewHolder;
            if (rowView == null) {

                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.item, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.DataText = (TextView) rowView.findViewById(R.id.bookText);
                viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
                viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);
                rowView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //viewHolder.DataText.setText(parkingList.get(position).getDescription());
            viewHolder.DataText.setText(parkingList.get(position).getDescription() + "" +parkingList.get(position).getPrice()+""+parkingList.get(position).getRestaurant_name());

            //String urlImage = parkingList.get(position).getImagePath();
            String urlImage = parkingList.get(position).getImgname();

            Log.d("url","url "+urlImage);
            //Glide.with(getApplicationContext()).load(urlImage).into(viewHolder.cardImage);
            Picasso.get().load(urlImage).error(R.drawable.ic_launcher_background).fit().centerCrop().into(viewHolder.cardImage);
            return rowView;
        }
    }// end of myAdapter


    public void getUrls(){

        for(int i=1;i<=1;i++) {
            getIp ip = new getIp();
            String del = ip.getIp();

            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
            //String URL = "http://192.168.0.103:8080/addSwipe";
            String URL = ""+del+":8080/addSwipe";

            final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("test", "NULL");
            } catch (Exception e) {
                e.printStackTrace();
            }
            final String requestBody = jsonObject.toString();
            Log.d("str", "str is" + requestBody);
            ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    System.out.print("Bool" + result);
                    if (result != null) {
                        //String[] load = result.split(",");
                        //String ip= null, index1 = null, index2 = null, description = null;

                        String ip, restaurant_name = null, category = null, price = null,description = null, rid=null, imgname = null;

                        try {
                            JSONObject jsonObject1 = new JSONObject(result);
                            ip = jsonObject1.getString("ip");
                            restaurant_name = jsonObject1.getString("restaurant_name");
                            category = jsonObject1.getString("category");
                            imgname = jsonObject1.getString("imgname");
                            price = jsonObject1.getString("price");
                            rid = jsonObject1.getString("rid");
                            description = jsonObject1.getString("description");

                            System.out.println("ip : "+ip+" restaurant_name : "+restaurant_name+" category = "+category+" price = "+price+" rid = "+rid );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //array.add(new Data(ip,index1,index2,description));
//                        Log.d("get url rest name",restaurant_name);
//                        Log.d("get url category ",category);
//                        Log.d("get url img ",imgname);
//                        Log.d("get url rid ",rid);
//                        Log.d("get url price ",price);
//                        Log.d("get url desc ",description);


                        array.add(new Data(restaurant_name,category,imgname,price,description,rid));
                        Log.d("array",""+array);


                        //Log.d("Result", "Result is " + result +" and "+ array);
                        myAppAdapter = new MyAppAdapter(array, getActivity());
                        flingContainer.setAdapter(myAppAdapter);

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "please try again." + error,
                            Toast.LENGTH_LONG);
                    toast.show();
                }

            });
        }

    }//end of getUrls()
















    public void rightSwipe(String restaurant_name,String category, String imgname, String rid){
        getIp ip = new getIp();
        String del = ip.getIp();

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        String URL = ""+del+":8080/swipe";

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", "leeaanair@gmail.com");
            jsonObject.put("restaurant_name",restaurant_name);
            jsonObject.put("category",category);
            jsonObject.put("image",imgname);
            jsonObject.put("rid",rid);
            jsonObject.put("swipe","1");
            Log.d("okay",rid);

        } catch (Exception e) {
            e.printStackTrace();
        }
        final String requestBody = jsonObject.toString();
        Log.d("swipe", "swipe str is" + requestBody);
        ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                //System.out.print("Bool" + result);
                if (result.equals("1")) {
//                    String ip= null, restaurant_name = null, category = null, imgname = null, rid = null,description = null;
//                    try {
//                        JSONObject jsonObject1 = new JSONObject(result);
//                        ip = jsonObject1.getString("ip");
//                        restaurant_name = jsonObject1.getString("restaurant_name");
//                        category = jsonObject1.getString("category");
//                        imgname = jsonObject1.getString("image");
//                        rid = jsonObject1.getString("rid");
//                        description = jsonObject1.getString("description");
//
//                        System.out.println("ip :"+ip+ "restaurant_name : "+restaurant_name+"category = "+category+"image name = "+imgname+"rid = "+rid );
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    array.add(new Data(restaurant_name,category,imgname,rid,description));
//                    Log.d("Result", "Result is " + result +" and "+ array);
//                    myAppAdapter = new MyAppAdapter(array, getActivity());
//                    flingContainer.setAdapter(myAppAdapter);
                    Toast.makeText(getActivity().getApplicationContext(), "Inserted into WishList", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                        "please try again." + error,
                        Toast.LENGTH_LONG);
                toast.show();
            }

        });




    }
}
