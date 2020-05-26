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

import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    EditText PhoneNo, Password;
    Button Login;
    LinearLayout AskSignUp;
    String password, phoneno;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    DatabaseReference myRef;

    AuthDetails authDetails;
    UserDetails userDetails;

    Dialog LoaderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        sharedPreferences = getSharedPreferences("SomebodySharedPreferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        PhoneNo = (EditText)findViewById(R.id.etenaphoneno);
        Password = (EditText)findViewById(R.id.etenapassword);
        Login = (Button) findViewById(R.id.btnlogin);
        AskSignUp = (LinearLayout)findViewById(R.id.llasksignup);

        myRef = FirebaseDatabase.getInstance().getReference();

        AskSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EnterNumberActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phoneno = PhoneNo.getText().toString().trim();
                password = Password.getText().toString().trim();

                if (phoneno.length() != 10){
                    Toast.makeText(LoginActivity.this, "Please enter a valid 10-digit phone number!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Password.getText().toString().trim().length() == 0){
                    Toast.makeText(LoginActivity.this, "Please enter a password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                LoaderDialog = new Dialog(LoginActivity.this);
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
                            authDetails = dataSnapshot.child(""+phoneno).getValue(AuthDetails.class);
                            if (authDetails.getPassword().equals(hashPassword())){
                                Query query1 = myRef.child("UserDetails");
                                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(phoneno)){
                                            userDetails = dataSnapshot.child(phoneno).getValue(UserDetails.class);
                                            editor.putString("UserDetails", new Gson().toJson(userDetails));
                                            editor.putInt("IsSignedIn", 1);
                                            editor.putInt("SetUp", 1);
                                            editor.apply(); editor.commit();

                                            LoaderDialog.dismiss();
                                            Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else{
                                            editor.putInt("IsSignedIn", 1);
                                            editor.apply(); editor.commit();
                                            Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                                            LoaderDialog.dismiss();
                                            Intent intent = new Intent(getApplicationContext(), ProfileSetupActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        LoaderDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Sorry, there was an error!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                                LoaderDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Sorry, incorrect password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            if (LoaderDialog.isShowing()){ LoaderDialog.dismiss(); }
                            Toast.makeText(LoginActivity.this, "Sorry, you aren't registered wit us, Please Sign Up!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(LoginActivity.this, "Error! Please try again later!", Toast.LENGTH_SHORT).show();
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
