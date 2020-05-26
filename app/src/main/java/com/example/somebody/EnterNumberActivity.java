package com.example.somebody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EnterNumberActivity extends AppCompatActivity {

    EditText PhoneNo, Password, RePassword;
    Button SendOtp;
    LinearLayout linearLayout;

    String phoneno, password;
    Dialog LoaderDialog;

    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_number);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        PhoneNo = (EditText)findViewById(R.id.etenaphoneno);
        Password = (EditText)findViewById(R.id.etenapassword);
        RePassword = (EditText)findViewById(R.id.etenarepassword);
        SendOtp = (Button)findViewById(R.id.btnsendotp);
        linearLayout = (LinearLayout)findViewById(R.id.llasklogin);

        myRef = FirebaseDatabase.getInstance().getReference();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Toast.makeText(this, "User Exists", Toast.LENGTH_SHORT).show();
        }

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        SendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneno = PhoneNo.getText().toString().trim();
                if (phoneno.length() != 10){
                    Toast.makeText(EnterNumberActivity.this, "Please enter a valid 10-digit phone number!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Password.getText().toString().trim().equals(RePassword.getText().toString().trim())){
                    Toast.makeText(EnterNumberActivity.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Password.getText().toString().trim().length()<8){
                    Toast.makeText(EnterNumberActivity.this, "Password should be atleast 8 characters long!", Toast.LENGTH_SHORT).show();
                    return;
                }

                LoaderDialog = new Dialog(EnterNumberActivity.this);
                View vieww = getLayoutInflater().inflate(R.layout.loader_layout, null);
                ProgressBar progressBar = (ProgressBar) vieww.findViewById(R.id.spinKit);
                DoubleBounce doubleBounce = new DoubleBounce();
                progressBar.setIndeterminateDrawable(doubleBounce);
                LoaderDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                LoaderDialog.setContentView(vieww);
                LoaderDialog.show();

                Query query = myRef.child("AuthDetails");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         if (dataSnapshot.hasChild(phoneno)){
                             LoaderDialog.dismiss();
                             Toast.makeText(EnterNumberActivity.this, "You're already registered with us, Please Login!", Toast.LENGTH_SHORT).show();
                         }
                         else{
                             password = Password.getText().toString().trim();
                             password = hashPassword();

                             LoaderDialog.dismiss();
                             Toast.makeText(EnterNumberActivity.this, "Sending OTP...", Toast.LENGTH_SHORT).show();
                             Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                             intent.putExtra("PhoneNumber", phoneno);
                             intent.putExtra("Password", password);
                             startActivity(intent);
                         }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError databaseError) {
                         LoaderDialog.dismiss();
                         Toast.makeText(EnterNumberActivity.this, "Error! Please try again later!", Toast.LENGTH_SHORT).show();
                     }
                });
            }
        });
    }

    String hashPassword(){
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());
            byte[] bytes = messageDigest.digest();

            for (int i=0 ; i<bytes.length ; i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            Toast.makeText(this, "There was an error!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return  sb.toString();
    }
}
