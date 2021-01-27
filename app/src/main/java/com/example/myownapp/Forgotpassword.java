package com.example.myownapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgotpassword extends AppCompatActivity {
    EditText useremail;
    Button userpass;
    ProgressBar pbar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        useremail=(EditText)findViewById(R.id.forgotemail);

        firebaseAuth=FirebaseAuth.getInstance();
    }

    public void sent(View view) {
        pbar.setVisibility(View.VISIBLE);
        firebaseAuth.sendPasswordResetEmail(useremail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pbar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(Forgotpassword.this, "Password send to your email", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(Forgotpassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }

            }

        });
    }
}
