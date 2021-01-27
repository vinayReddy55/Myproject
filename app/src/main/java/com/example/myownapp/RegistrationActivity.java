package com.example.myownapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myownapp.utilitis.FirebaseUtilitis;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends AppCompatActivity {
    @BindView(R.id.rgemailed)
    TextInputEditText rgemail;
    @BindView(R.id.rgpswded)
    TextInputEditText rgpass;

    FirebaseUtilitis firebaseUtilitis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife .bind(this);
        firebaseUtilitis =new FirebaseUtilitis(this);
    }
    @OnClick(R.id.rgbtn)
    void register(){
        String email=rgemail.getText().toString();
        String password=rgpass.getText().toString();
        if(email.length() ==0 || !email.contains("@")){
            rgemail.setError("Enter Valid Email");
            return;
        }
        if(password.length()<6){
            rgpass.setError("Password Must to 6 Charecters");
            return;
        }
        firebaseUtilitis.register(email,password);
    }
    @OnClick(R.id.rgbtn2)
    void goTologin(){
        Intent intent= new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}