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

public class ProfileFragment extends Fragment {

    Button btnLogin, btnRegister;
    TextView name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.profile, container, false);

        name = view.findViewById(R.id.name);
        name.setText("Sarah");


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

