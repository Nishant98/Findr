package com.example.sarah.nav;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment {
    private MyAppAdapter myAppAdapter;
    ArrayList<Data> array;
    SwipeFlingAdapterView flingContainer;
    String del;
    String email_session="";
    SessionManager sessionManager;
    //ShimmerFrameLayout mShimmerViewContainer;

    //ProgressBar loading;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       // mProgressBar = getActivity().findViewById(R.id.progressBar);
        //mProgressBar.setVisibility(View.VISIBLE);
        myAppAdapter = new MyAppAdapter(array, getActivity());
        return inflater.inflate(R.layout.home,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        array = new ArrayList<>();
        getIp ip = new getIp();
        //loading = getActivity().findViewById(R.id.loading_home);
        //mProgressBar.setVisibility(View.VISIBLE);
        //mShimmerViewContainer = getActivity().findViewById(R.id.shimmer_view_container);


        sessionManager = new SessionManager(getActivity().getApplicationContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        email_session = user.get(sessionManager.EMAIL);
        del = ip.getIp();

        array.add(new Data("Guddu ka Dhaaba", "apple pie", "apple pie_68383.jpg", "50", "Lorem ipsum dolor sit amet, consectetur adipiscing elit,","1"));

        getUrls(email_session);

        flingContainer = (SwipeFlingAdapterView) getActivity().findViewById(R.id.frame);
//        refresh_button = getActivity().findViewById(R.id.refresh_button);
//        refresh_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent r = new Intent(getContext(),MainActivity.class);
////                startActivity(r);
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
//                myAppAdapter.notifyDataSetChanged();
//                Toast.makeText(getActivity().getApplicationContext(), "REEEEE", Toast.LENGTH_SHORT).show();
//            }
//        });


        final SwipeRefreshLayout refresh = (SwipeRefreshLayout) getActivity().findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getActivity().getApplicationContext(), "REEEEE", Toast.LENGTH_SHORT).show();
                        myAppAdapter.notifyDataSetChanged();
                        refresh.setRefreshing(false);
                    }
                }, 1000);
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
                    }

                    @Override
                    public void onRightCardExit(Object dataObject) {

                        Data a=array.remove(0);
                        Log.d("array remove", String.valueOf(a));

                        String restaurant_name=a.getRestaurant_name();
                        String category = a.getCategory();
                        String imgname = a.getImgname();
                        String rid = a.getRid();
                        rightSwipe(restaurant_name,category,imgname,rid);

                myAppAdapter.notifyDataSetChanged();
                //Toast.makeText(getActivity().getApplicationContext(),"RiGHT",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                getUrls(email_session);
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });
        refresh.setEnabled(true);

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

            viewHolder.DataText.setText("Restaurant Name: "+parkingList.get(position).getRestaurant_name() + "\n\n" +"Food Name: "+parkingList.get(position).getCategory()+"\n\n"+"Description:\n"+parkingList.get(position).getDescription()+"\n\n\n"+"Price: ₹"+parkingList.get(position).getPrice());

            //String urlImage = parkingList.get(position).getImagePath();
            String urlImage = del+":8080/images/"+parkingList.get(position).getRid()+"/"+parkingList.get(position).getCategory()+"/"+parkingList.get(position).getImgname();
            //Log.d("url","url "+urlImage);

            Picasso.get().load(urlImage).error(R.drawable.ic_launcher_background).fit().centerCrop().into(viewHolder.cardImage);
            return rowView;
        }
    }// end of myAdapter


    public void getUrls(String email){


//        myAppAdapter.notifyDataSetChanged();

        for(int i=1;i<=1;i++) {
            getIp ip = new getIp();
            String del = ip.getIp();

            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
            String URL = ""+del+":8080/addSwipe";

            final JSONObject jsonObject = new JSONObject();
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
                    System.out.print("Bool" + result);
                    if (result != null) {
                        String ip, restaurant_name = null, category = null, price = null,description = null, rid=null, imgname = null;
                        try {
                            Log.d("result    ",""+result);

                            JSONObject jsonObject1 = new JSONObject(result);
                            ip = jsonObject1.getString("ip");
                            restaurant_name = jsonObject1.getString("restaurant_name");
                            category = jsonObject1.getString("category");
                            imgname = jsonObject1.getString("imgname");
                            price = jsonObject1.getString("price");
                            rid = jsonObject1.getString("rid");
                            description = jsonObject1.getString("description");
                            Log.d("description",""+jsonObject1);


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
                        myAppAdapter = new MyAppAdapter(array, getActivity());
                        flingContainer.setAdapter(myAppAdapter);
                        myAppAdapter.notifyDataSetChanged();
                        //mProgressBar.setVisibility(View.INVISIBLE);

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Volley needs attention" + error,
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            });

        }

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                loading.setVisibility(View.GONE);
//                myAppAdapter.notifyDataSetChanged();
//            }
//        },5000);

    }//end of getUrls()
















    public void rightSwipe(String restaurant_name,String category, String imgname, String rid){
        getIp ip = new getIp();
        String del = ip.getIp();

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        String URL = ""+del+":8080/swipe";

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email_session);
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
                    Toast.makeText(getActivity().getApplicationContext(), "Inserted into WishList", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                        "Volley needs attention." + error,
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}
