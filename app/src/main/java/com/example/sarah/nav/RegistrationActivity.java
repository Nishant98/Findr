package com.example.sarah.nav;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity {
    private Button register;
    private EditText email,password,city,name,contact;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email=findViewById(R.id.email);
        loading=findViewById(R.id.loading);
        password=findViewById(R.id.password);
        register=findViewById(R.id.register);
        city=findViewById(R.id.city);
        //pincode=findViewById(R.id.pincode);
        name=findViewById(R.id.name);
        contact=findViewById(R.id.contact);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail=email.getText().toString().trim();
                String mPassword=password.getText().toString().trim();
                String mName=name.getText().toString().trim();

                String mCity=city.getText().toString().trim();
                //String mPincode=pincode.getText().toString().trim();
                String mContact=contact.getText().toString().trim();



                if (!mEmail.isEmpty() || !mPassword.isEmpty() || !mName.isEmpty() || !mCity.isEmpty() ||  !mContact.isEmpty()) {
                    if(mContact.length()==10) {
                        Register(mEmail, mPassword, mName, mCity, mContact);
                    }
                }
                else{
                    email.setError("Please enter email");
                    password.setError("Please enter password");
                    name.setError("Please Enter Name");
                    city.setError("Please Enter City");
//                    pincode.setError("Please Enter Pincode");
                    contact.setError("Please enter Contact");
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    private void Register(String mEmail, String mPassword, String mName, String mCity, String mContact) {
        getIp ip = new getIp();
        String del = ip.getIp();
        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
        String URL = ""+del+":8080/register";

        //String URL = "http://192.168.0.103:8080/register";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", mEmail);
            jsonObject.put("password", mPassword);
            jsonObject.put("name", mName);
            jsonObject.put("city", mCity);
            //jsonObject.put("pincode", mPincode);
            jsonObject.put("contact", mContact);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        final String requestBody = jsonObject.toString();
        Log.d("str_register","strREG is"+requestBody);

        ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback(){
            @Override
            public void onSuccessResponse(String result) {
                Log.d("RESULT","RESULTS "+result);
                if (result.equals("1")) {
                    Toast.makeText(RegistrationActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegistrationActivity.this, "Ooops! Try Again.", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(RegistrationActivity.this,
                        "Volley needs attention" + error,
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}
