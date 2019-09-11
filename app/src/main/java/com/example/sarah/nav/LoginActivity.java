package com.example.sarah.nav;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("Registered")
public class LoginActivity extends AppCompatActivity {
    Button btnLogin, btnRegister;

    EditText email,password,name;
    ProgressBar loading;
    //final String URL="http://192.168.0.103:8080/login";

    //for shared preference
    private SharedPreferences mPref;
    private static final String PREF_NAME="email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //for shared preference
        mPref=getSharedPreferences(PREF_NAME,MODE_PRIVATE);

        btnLogin = findViewById(R.id.loginButton);
        btnRegister = findViewById(R.id.registerButton);
        email=findViewById(R.id.email);
        loading=findViewById(R.id.loading);
        password=findViewById(R.id.password);


        //for shared preference
        String storedEmail = mPref.getString("EMAIL","");
        email.setText(storedEmail);


        //LOGIN BUTTON LOGIC
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail=email.getText().toString().trim();
                String mPassword=password.getText().toString().trim();

                if (!mEmail.isEmpty() || !mPassword.isEmpty()) {
                    //shared pref
                    SharedPreferences.Editor editor=mPref.edit();
                    editor.putString("EMAIL",mEmail);
                    editor.apply();

                    Login(mEmail,mPassword);
                    //shared pref
                    email.setText("");
                    password.setText("");
                    Toast.makeText(LoginActivity.this,"Saved",Toast.LENGTH_SHORT).show();
                }
                else{
                    email.setError("Haggu email daal");
                    password.setError("Haggu Password daal");
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

    }

    private void Login(final String email, final String password) {
        //name=findViewById(R.id.name);
        getIp ip = new getIp();
        String del = ip.getIp();
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        //String URL = "http://192.168.0.103:8080/login";
        String URL = ""+del+":8080/login";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        final String requestBody = jsonObject.toString();
        Log.d("str","str is"+requestBody);

        ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback(){
                    @Override
                    public void onSuccessResponse(String result) {

                        //System.out.print("Bool" + result);
                        Log.d("RESULT","RESULTS "+result);
                        if (result.equals("1")) {
                            loading.setVisibility(View.VISIBLE);
                             Toast.makeText(LoginActivity.this, "Hello "+email, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("params", email);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Ooops! Try Again.", Toast.LENGTH_SHORT).show();
                        }
                    }
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(LoginActivity.this,
                        "Volley needs attention" + error,
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });






















        /*
        loading.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String success =jsonObject.getString("success");
                            JSONArray jsonArray=jsonObject.getJSONArray("Login");
                            if(success.equals('1')){
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject object=jsonArray.getJSONObject(i);
                                    String name=object.getString("name").trim();
                                    String email=object.getString("email").trim();
                                    Toast.makeText(LoginActivity.this,"Successful Login"+name+"",Toast.LENGTH_SHORT).show();
                                    loading.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this,"Hagg diya"+e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this,"Successful Login"+error.toString(),Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("email",email);
                params.put("password",password);

                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    */
}}

