package com.example.myownapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myownapp.Adapters.PostAdapter;
import com.example.myownapp.Models.PostModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Viewactivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView1,textView2;
    String Tiitle,description;
    int images;
    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    boolean isFirstTime=true;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewactivity);


//        imageView=findViewById(R.id.viewactivityimage);
        textView1=findViewById(R.id.viewactivitytitle);
        textView2=findViewById(R.id.viewactivitydescription);
        firebaseFirestore=FirebaseFirestore.getInstance();





        storageReference= FirebaseStorage.getInstance().getReference("posts");

        StorageReference profileref=storageReference.child("My_image"+getTimeStamp()+"jpg");
        profileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });

        //  imageView.setImageResource(getIntent().getIntExtra("image_id",0));
     Tiitle =getIntent().getStringExtra("Title");
     description=getIntent().getStringExtra("Description");
      textView1.setText(Tiitle);
      textView2.setText(description);

    }

   private String getTimeStamp() {
      java.text.SimpleDateFormat simpleDateFormat= new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        return  simpleDateFormat.format(new Date());
    }
}