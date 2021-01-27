package com.example.myownapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.myownapp.utilitis.FirebaseUtilitis;

import static com.example.myownapp.MainActivity.closeDrawer;
import static com.example.myownapp.MainActivity.redirectActivity;

public class About extends AppCompatActivity {
    DrawerLayout drawerLayout;
    FirebaseUtilitis firebaseUtilitis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        drawerLayout=findViewById(R.id.drawer_layout);
    }
    public  void  Clickmenu(View view){
        //open drawer
        MainActivity.openDrawer(drawerLayout);
    }
    public  void  Clicklogo(View view){
        //close drawer
        closeDrawer(drawerLayout);
    }
    public  void ClickHome(View view){
        //Redirect activity
        redirectActivity(this,MainActivity.class);

    }
    public  void ClickDashboard(View view){
        //Redirect activity dashboard
        MainActivity.redirectActivity(this,About.class);


    }
    public  void ClickAboutus(View view){
        //Recreate activity
        recreate();

    }
    public  void ClickLogout(View view){
        firebaseUtilitis.logout();
    }
    @Override
    protected  void onPause(){
        super.onPause();
        //close drawer
        MainActivity.closeDrawer(drawerLayout);
    }

    public void insta(View view) {
        Intent browseintent =new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/saisharanya7/"));
        startActivity(browseintent);
    }
}