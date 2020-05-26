package com.example.somebody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    Button Login;
    EditText Otp1, Otp2, Otp3, Otp4, Otp5, Otp6;
    String phoneno, password, codeSent;
    TextView DisplayPhone;
    LinearLayout Resend;
    ProgressDialog mProgressDialog;

    FirebaseAuth mAuth;

    Dialog dialog;
    DatabaseReference myRef;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    AuthDetails authDetails;

    List<AuthUI.IdpConfig> providers;

    Dialog LoaderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Login = (Button)findViewById(R.id.btnverify);
        Otp1 = (EditText)findViewById(R.id.etotp1);
        Otp2 = (EditText)findViewById(R.id.etotp2);
        Otp3 = (EditText)findViewById(R.id.etotp3);
        Otp4 = (EditText)findViewById(R.id.etotp4);
        Otp5 = (EditText)findViewById(R.id.etotp5);
        Otp6 = (EditText)findViewById(R.id.etotp6);
        DisplayPhone = (TextView)findViewById(R.id.tvdisplayphoneno);
        Resend = (LinearLayout)findViewById(R.id.llresend);

        sharedPreferences = getSharedPreferences("SomebodySharedPreferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference().child("AuthDetails");

        phoneno = getIntent().getStringExtra("PhoneNumber");
        password = getIntent().getStringExtra("Password");

        sendVerificationCode();

        //providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(), new AuthUI.IdpConfig.PhoneBuilder().build(), new AuthUI.IdpConfig.FacebookBuilder().build(), new AuthUI.IdpConfig.GoogleBuilder().build());

        DisplayPhone.setText(phoneno);

        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoaderDialog = new Dialog(SignUpActivity.this);
                View vieww = getLayoutInflater().inflate(R.layout.loader_layout, null);
                ProgressBar progressBar = (ProgressBar) vieww.findViewById(R.id.spinKit);
                DoubleBounce doubleBounce = new DoubleBounce();
                progressBar.setIndeterminateDrawable(doubleBounce);
                LoaderDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                LoaderDialog.setContentView(vieww);
                LoaderDialog.setCancelable(false);
                LoaderDialog.show();

                verifySignIn();
            }
        });
    }

    private void sendVerificationCode(){

        String phno = "+91"+phoneno;

        if(phno.length()!=13){
            Toast.makeText(this, "Please Enter a valid phone number!", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                codeSent = s;
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();

                if (code != null){
                    Otp1.setText(""+code.charAt(0));
                    Otp2.setText(""+code.charAt(1));
                    Otp3.setText(""+code.charAt(2));
                    Otp4.setText(""+code.charAt(3));
                    Otp5.setText(""+code.charAt(4));
                    Otp6.setText(""+code.charAt(5));

                    if(!Otp6.getText().toString().equals("")){
                        Login.performClick();
                    }
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                LoaderDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Sorry, Verification failed!", Toast.LENGTH_SHORT).show();
            }

        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phno, 90, TimeUnit.SECONDS, SignUpActivity.this, mCallbacks );

    }

    private void verifySignIn(){

        String code = Otp1.getText().toString().trim()+Otp2.getText().toString().trim()+Otp3.getText().toString().trim()+Otp4.getText().toString().trim()+Otp5.getText().toString().trim()+Otp6.getText().toString().trim();
        if (code.length() != 6){
            LoaderDialog.dismiss();
            Toast.makeText(this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        LoaderDialog.dismiss();
                        if (task.isSuccessful()) {
                            authDetails = new AuthDetails();
                            authDetails.setPhoneNumber(""+phoneno);
                            authDetails.setPassword(""+password);

                            myRef.child(phoneno).setValue(authDetails);

                            editor.putBoolean("IsSignedIn", true);
                            editor.putString("PhoneNumber", phoneno);
                            editor.commit();
                            editor.apply();

                            Toast.makeText(SignUpActivity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), ProfileSetupActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), "Error in Login!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    void showSignInOptions(){
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.MyTheme)
                .build(), 3000
        );
    }
}
