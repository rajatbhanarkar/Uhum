package com.example.somebody;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnterNumberActivity extends AppCompatActivity {

    Button SendOTP;
    EditText PhoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_number);

        SendOTP = (Button)findViewById(R.id.btnsendotp);
        PhoneNo = (EditText)findViewById(R.id.etenaphoneno);

        SendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneno =
                Toast.makeText(EnterNumberActivity.this, "Sending OTP...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("PhoneNumber",PhoneNo.getText().toString());
                startActivity(intent);
            }
        });
    }
}
