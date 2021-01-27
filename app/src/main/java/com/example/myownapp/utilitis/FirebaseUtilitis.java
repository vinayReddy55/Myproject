package com.example.myownapp.utilitis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myownapp.LoginActivity;
import com.example.myownapp.MainActivity;
import com.example.myownapp.Models.PostModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FirebaseUtilitis {
    Context context;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDilog;
    FirebaseStorage firebaseStorage;
    FirebaseFirestore firebaseFirestore;
    Activity activity;

    public FirebaseUtilitis(Context context) {
        this.context = context;
        firebaseAuth=FirebaseAuth.getInstance();
        progressDilog=new ProgressDialog(context);
        progressDilog.setMessage("Loading please wait...");
        progressDilog.setCanceledOnTouchOutside(false);
        firebaseStorage=FirebaseStorage.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        activity= (Activity) context;

    }
    public  void register(String email,String password){
        progressDilog.setMessage("Creating account......");
        progressDilog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDilog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(context, "Account created sucessfully", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDilog.dismiss();
                e.printStackTrace();
                Toast.makeText(context, "Failed due to"+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    public  void login(String email,String password){
        progressDilog.setMessage("Login into account......");
        progressDilog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDilog.dismiss();
                if (task.isSuccessful()){

                    Toast.makeText(context, "Login sucessfully", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(context, MainActivity.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   context.startActivity(intent);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDilog.dismiss();
                e.printStackTrace();
                Toast.makeText(context, "Failed due to"+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    String getTimeStamp(){
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        return  simpleDateFormat.format(new Date());
    }
   public void uploadImage(Uri uri, PostModel postModel, boolean b){
        progressDilog.show();
        StorageReference storageReference=firebaseStorage.getReference().child("posts").child("My_image"+getTimeStamp()+"jpg");
        storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDilog.dismiss();
                            Log.d("storageReference URL","onSucess:"+uri.toString());
                            postModel.setImageUrl(uri.toString());
                            post(postModel);

                        }
                    });

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDilog.dismiss();
                e.printStackTrace();
                Toast.makeText(context, "Image Upload Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    public  void  post( PostModel postModel){
        progressDilog.show();
        DocumentReference documentReference=firebaseFirestore.collection("Posts").document();
        postModel.setDocID(documentReference.getId());
        documentReference.set(postModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDilog.dismiss();
                Toast.makeText(context, "Poasted Sucessfully", Toast.LENGTH_SHORT).show();
                activity.finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDilog.dismiss();
                e.printStackTrace();
                Toast.makeText(context, "Post Upload Failed", Toast.LENGTH_SHORT).show();

            }
        });

    }
    public  void deleteImageByUrl(Uri uri,PostModel postModel,boolean isFromUpdate){
        progressDilog.show();
        progressDilog.setMessage("Image deleting...");
        firebaseStorage.getReferenceFromUrl(postModel.getImageUrl()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDilog.dismiss();
                if (isFromUpdate){
                    uploadImage(uri,postModel,true);
                }else{
                    deletePost(postModel);
                }



                }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDilog.dismiss();
                e.printStackTrace();
                Toast.makeText(context, "Post image deletion failed", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public  void  deletePost(PostModel postModel){
        progressDilog.show();
        progressDilog.setMessage("Post  deleting...");
        firebaseFirestore.collection("Posts").document(postModel.getDocID()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDilog.dismiss();
                Toast.makeText(context, "Post Deleted Successfully", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDilog.dismiss();
                e.printStackTrace();
                Toast.makeText(context, "Post Deletion Failed", Toast.LENGTH_SHORT).show();

            }
        });
    }
   public  void logout(){
        firebaseAuth.signOut();
        progressDilog.dismiss();
        Intent intent= new Intent(context, LoginActivity.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
       context.startActivity(intent);
   }
}
