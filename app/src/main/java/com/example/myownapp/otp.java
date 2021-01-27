package com.example.myownapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class otp extends AppCompatActivity {
    Button verifyOTP;
    TextInputEditText textInputEditText;
    TextView resendOTP;

    String otp;

    String verifyID,phoneNumber;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        verifyOTP = findViewById(R.id.verifyOTP);
        textInputEditText = findViewById(R.id.OTPEditText);

        phoneNumber = getIntent().getExtras().getString("phoneNumber");
        verifyID = getIntent().getExtras().getString("verificationID");

        resendOTP = findViewById(R.id.resendOTP);
        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = textInputEditText.getText().toString();
                if (otp.length() < 6){
                    Toast.makeText(otp.this, "OTP must be 6 digits", Toast.LENGTH_SHORT).show();
                }else{
                    verifyOTP();
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
                Toast.makeText(otp.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationID, forceResendingToken);
                verifyID = verificationID;
                Toast.makeText(otp.this, "OTP Resent", Toast.LENGTH_SHORT).show();

            }
        };

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendotp();

            }
        });
    }

    void resendotp(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNumber,
                30,
                TimeUnit.SECONDS,
                this,
                onVerificationStateChangedCallbacks
        );
    }

    void verifyOTP(){
        PhoneAuthCredential phoneAuthCredential =  PhoneAuthProvider.getCredential(verifyID,otp);
        signInWithCredetials(phoneAuthCredential);
    }

    void signInWithCredetials(PhoneAuthCredential phoneAuthCredential){

        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(otp.this, "Sign in Successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(otp.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(otp.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}