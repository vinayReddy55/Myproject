package com.example.myownapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Activityphonenumber extends AppCompatActivity {
    TextInputEditText phoneNumberEditText;
    Button sendOTP;
    String phoneNumber;

    String verifyID;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activityphonenumber);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        sendOTP = findViewById(R.id.sendOTP);
        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = phoneNumberEditText.getText().toString();
                if (phoneNumber.length() < 10){
                    Toast.makeText(Activityphonenumber.this, "Enter valid mobile number", Toast.LENGTH_SHORT).show();
                }else{
                    sendOTP();
                }
            }
        });
        onVerificationStateChangedCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //auto verification
                signInWithCredetials(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(Activityphonenumber.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationID, forceResendingToken);
                verifyID = verificationID;
                Toast.makeText(Activityphonenumber.this, "Sent OTP", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activityphonenumber.this,otp.class);
                intent.putExtra("verificationID",verificationID);
                intent.putExtra("phoneNumber",phoneNumber);
                startActivity(intent);
            }
        };
    }

    private void signInWithCredetials(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Activityphonenumber.this, "Sign in Successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Activityphonenumber.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Activityphonenumber.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    void sendOTP() {
         PhoneAuthProvider.getInstance().verifyPhoneNumber(
                 "+91"+phoneNumber,
                 30,
                 TimeUnit.SECONDS,
                 this,
                 onVerificationStateChangedCallbacks
         );
    }
}