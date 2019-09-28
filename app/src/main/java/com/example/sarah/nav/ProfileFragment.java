package com.example.sarah.nav;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ProfileFragment extends Fragment {
    TextView name,email;
    String email_session="";
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.profile, container, false);

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        email_session = user.get(sessionManager.EMAIL);

//        name = view.findViewById(R.id.name);
//        name.setText("Sarah");

        email = view.findViewById(R.id.email);
        email.setText(email_session);


        return view;
    }
}
//        btnRegister = view.findViewById(R.id.registerButton);
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                Toast toast = Toast.makeText(getContext(), "This is login button", Toast.LENGTH_SHORT);
//                toast.show();
//            }
//        });
//
//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), RegistrationActivity.class);
//                startActivity(intent);
//            }
//        });