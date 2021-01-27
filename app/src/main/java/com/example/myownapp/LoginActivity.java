package com.example.myownapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.myownapp.utilitis.FirebaseUtilitis;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    LinearLayout L1,L2;
    RelativeLayout R1;

    @BindView(R.id.lgemailed)
    TextInputEditText lgemail;
    @BindView(R.id.lgpswded)
    TextInputEditText lgpass;

    FirebaseUtilitis firebaseUtilitis;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

           L1 .setVisibility(View.VISIBLE);
           R1.setVisibility(View.VISIBLE);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        firebaseUtilitis =new FirebaseUtilitis(this);
        //for splash screen
        L1=findViewById(R.id.layout2);
        R1=findViewById(R.id.layout1);
        handler.postDelayed(runnable, 3000);
        //for progress bar
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

    }
    @OnClick(R.id.lgbtn)
    void register(){
        String email=lgemail.getText().toString();
        String password=lgpass.getText().toString();
        if(email.length() ==0 || !email.contains("@")){
            lgemail.setError("Enter Valid Email");
            return;
        }
        if(password.length()<6){
            lgpass.setError("Password Must to 6 Charecters");
            return;
        }
        firebaseUtilitis.login(email,password);
    }
    @OnClick(R.id.lgbtn2)
    void goTologin(){
        Intent intent= new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void forgot(View view) {
        Intent intent= new Intent(this, Forgotpassword.class);
        startActivity(intent);

    }

    public void phoneauth(View view) {
       Intent intent= new Intent(this, Activityphonenumber.class);
        startActivity(intent);
    }
}