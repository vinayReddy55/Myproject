package com.example.myownapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myownapp.Adapters.PostAdapter;
import com.example.myownapp.Models.PostModel;
import com.example.myownapp.myinterfaces.RefreshList;
import com.example.myownapp.utilitis.FirebaseUtilitis;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity  {
    //initalize variable
    private CircleImageView profileimage;
     int PICK_IMAGE_CODE=100;
    private static  final  int PERMISSION_CODE=100;
    Uri selectimageUri;
    StorageReference storageReference;

    DrawerLayout drawerLayout;
  FirebaseUtilitis firebaseUtilitis;


  ProgressDialog progressDilog;
   List<PostModel> postModelList;
   ListenerRegistration listenerRegistration;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    PostAdapter postAdapter;
    boolean isFirstTime=true;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //Assign variable
        firebaseAuth=FirebaseAuth.getInstance();


        drawerLayout=findViewById(R.id.drawerlayout);
        //profile pic reteive from fire base and showon imageview
        storageReference= FirebaseStorage.getInstance().getReference();

        StorageReference profileref=storageReference.child("Profile.jpg");
        profileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileimage);
            }
        });

        firebaseUtilitis=new FirebaseUtilitis(this);

        progressDilog=new ProgressDialog(this);
        progressDilog.setMessage(" please wait...");
        progressDilog.setCanceledOnTouchOutside(false);

        profileimage=findViewById(R.id.circleimage);

        postModelList =new ArrayList<>();
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){
                        //permisssin not granted request it
                        String[]permission={Manifest.permission.READ_EXTERNAL_STORAGE};
                        //Show popup for permisiion
                        requestPermissions(permission,PERMISSION_CODE);

                    }else {
                        //permission already granted
                        pickImageFromGallery();

                    }
                }else {
                    pickImageFromGallery();
                }
            }
        });
    }
//nav pic image code
    private void pickImageFromGallery() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent,PICK_IMAGE_CODE);


    }
    //handle reusult of runtime permisiion


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_CODE && grantResults.length>0){
            pickImageFromGallery();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CODE) {
            //set iamge to imageview
         selectimageUri = data.getData();
           // profileimage.setImageURI(selectimageUri);
            uploadprofilepic( selectimageUri );
        }
    }

    private void uploadprofilepic(Uri  selectimageUri ) {
        StorageReference fileref=storageReference.child("Profile.jpg");
        fileref.putFile(selectimageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                  @Override
                  public void onSuccess(Uri uri) {
                      Picasso.get().load(uri).into(profileimage);
                  }
              });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public  void  Clickmenu(View view){
        //open drawer
        openDrawer(drawerLayout);

    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        //open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public  void  Clicklogo(View view){
        //close drawer
        closeDrawer(drawerLayout);

    }



    public static void closeDrawer(DrawerLayout drawerLayout) {
        //close dawer layout
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            //when drawer is open
            //close drwer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public  void ClickHome(View view){
        //Recreate activity
        recreate();
    }
    public  void ClickDashboard(View view){
        //Recreate activity
        redirectActivity(this,Dashboard.class);

    }
    public  void ClickAboutus(View view){
        //Recreate activity
        redirectActivity(this,About.class);

    }

    public  void ClickLogout(View view) {
        new AlertDialog.Builder(MainActivity.this).setMessage("Action")
                .setMessage("Confirm sign out")
                .setPositiveButton("Cancel", null)

                .setNegativeButton("Sign out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        firebaseUtilitis.logout();
                        progressDilog.show();
                        progressDilog.setMessage("Sign out...");

                    }

                }).create().show();

    }

    public static void redirectActivity(Activity activity,Class aclass) {
        //Intilize intent
        Intent intent =new Intent(activity,aclass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected  void onPause(){
        super.onPause();
        //close drawer
        closeDrawer(drawerLayout);
    }
    void fetchDataFromFireStore2(){
        postModelList.clear();
       progressDilog.show();
      CollectionReference collectionReference= FirebaseFirestore.getInstance().collection("Posts");
        listenerRegistration = collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    progressDilog.dismiss();
                    error.printStackTrace();
                    Toast.makeText(MainActivity.this, "Signout sucessfully", Toast.LENGTH_SHORT).show();
                    return;
                }
              for(DocumentChange documentChange : value.getDocumentChanges()){
                  PostModel postModel=documentChange.getDocument().toObject(PostModel.class);
                  int oldIndex=documentChange.getOldIndex();
                  int newIndex=documentChange.getNewIndex();
                  if(documentChange.getType() == DocumentChange.Type.ADDED){
                      if(!isFirstTime){
                          //not first time
                          postModelList.add(newIndex,postModel);
                          postAdapter.notifyItemInserted(newIndex);
                          postAdapter.notifyItemRangeChanged(0,postModelList.size());

                      }else{
                          postModelList.add(postModel);
                      }

                  }
                  if(documentChange.getType() == DocumentChange.Type.MODIFIED){
                      if(newIndex==oldIndex){
                          //doc modified  but position not changed
                          postModelList.set(oldIndex,postModel);
                          postAdapter.notifyItemChanged(oldIndex);
                      }else {
                          //doc modified position  changed
                          postModelList.remove(oldIndex);
                          postModelList.set(newIndex,postModel);
                          postAdapter.notifyItemMoved(oldIndex,newIndex);
                      }
                      postAdapter.notifyDataSetChanged();

                  }
                  if(documentChange.getType() == DocumentChange.Type.REMOVED){
                      postModelList.remove(oldIndex);
                      postAdapter.notifyItemRemoved(oldIndex);

                  }
              }
              isFirstTime=false;
                postAdapter =new PostAdapter(postModelList,MainActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
               // recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                recyclerView.setAdapter(postAdapter);
               progressDilog.dismiss();
            }
        });
    }
    public void click(View view) {
        Intent intent=new Intent(this,postactivity.class);
        startActivity(intent);
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(listenerRegistration!=null){
            listenerRegistration.remove();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
      checkUserLoggedInOrNot();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout){
            firebaseUtilitis.logout();
        }
        return super.onOptionsItemSelected(item);
    }


    void checkUserLoggedInOrNot(){
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            //user not loged in
            startActivity(new Intent(this,LoginActivity.class));

        }else {
           if(listenerRegistration == null)
            fetchDataFromFireStore2();
        }


    }



}