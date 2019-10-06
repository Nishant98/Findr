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
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        btnLogin = findViewById(R.id.loginButton);
        btnRegister = findViewById(R.id.registerButton);
        email=findViewById(R.id.email);
        loading=findViewById(R.id.loading);
        password=findViewById(R.id.password);
        sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLoggin()){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

        //LOGIN BUTTON LOGIC
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail=email.getText().toString().trim();
                String mPassword=password.getText().toString().trim();

                if (!mEmail.isEmpty() || !mPassword.isEmpty()) {
                    Login(mEmail,mPassword);
                    //shared pref
                    email.setText("");
                    password.setText("");
                    Toast.makeText(LoginActivity.this,"Saved",Toast.LENGTH_SHORT).show();
                }
                else{
                    email.setError("Enter email");
                    password.setError("Enter password");
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
        getIp ip = new getIp();
        String del = ip.getIp();
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
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

                            sessionManager.createSession(email);

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
}}

