package com.example.myownapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myownapp.Models.PostModel;
import com.example.myownapp.utilitis.FirebaseUtilitis;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class postactivity extends AppCompatActivity {
    @BindView(R.id.picimg)
    ImageView pickimage;

    @BindView(R.id.psted1)
    TextInputEditText titleEditText;

    @BindView(R.id.psted2)
    TextInputEditText DescriptionEditText;

    int Pick_image=12;
    int Permissin_image=23;
    Uri selectimageUri;
    FirebaseUtilitis firebaseUtilitis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postactivity);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new ChasingDots();
        progressBar.setIndeterminateDrawable(doubleBounce);

        firebaseUtilitis=new FirebaseUtilitis(this);
        ButterKnife.bind(this);
        pickimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkRuntimePermission();

            }
        });
    }
     void  checkRuntimePermission(){
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Permissin_image);
            }
        else {
            pickimagefromgallery();
        }

             }else {
             pickimagefromgallery();
        }
     }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==Permissin_image && grantResults.length>0){
            pickimagefromgallery();
        }
    }

    void pickimagefromgallery(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,Pick_image);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Pick_image){
            selectimageUri  =data.getData();
            pickimage.setImageURI(selectimageUri);
        }
    }

    public void post(View view) {
        String title=titleEditText.getText().toString();
        String descripition=DescriptionEditText.getText().toString();

        if(selectimageUri==null){
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
            return;
        }
        if(title.length()==0){
            Toast.makeText(this, "Please Enter Title", Toast.LENGTH_SHORT).show();
            return;
        }
        if(descripition.length()<10){
            Toast.makeText(this, "Description Must be 10 Charecters", Toast.LENGTH_SHORT).show();
            return;
        }
        PostModel postModel=new PostModel();
        postModel.setTitle(title);
        postModel.setDescription(descripition);

        firebaseUtilitis.uploadImage(selectimageUri,postModel, true);

    }
}