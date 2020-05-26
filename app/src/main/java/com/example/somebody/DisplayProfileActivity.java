package com.example.somebody;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

public class DisplayProfileActivity extends AppCompatActivity {

    ImageView Call1, Call2, Sms, Email, WhatsApp, ProPic, Back;
    TextView UserName, Address;
    Button ConfirmHelp;

    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Call1 = (ImageView)findViewById(R.id.ivdpacall);
        Call2 = (ImageView)findViewById(R.id.ivdpacellalternate);
        Sms = (ImageView)findViewById(R.id.ivdpasms);
        Email = (ImageView)findViewById(R.id.ivdpaemail);
        WhatsApp = (ImageView)findViewById(R.id.ivdpawhatsapp);
        ProPic = (ImageView)findViewById(R.id.ivdpaprofilepic);
        Back = (ImageView)findViewById(R.id.ivdpaback);
        UserName = (TextView)findViewById(R.id.tvusername);
        Address = (TextView)findViewById(R.id.tvdpaaddress);
        //ConfirmHelp = (Button)findViewById(R.id.btndpaconfirmhelp);

        userDetails = (UserDetails)getIntent().getSerializableExtra("UserDetails");

        String propic = userDetails.getProfPic();

        if (propic.equals("Male")){
            ProPic.setImageResource(R.drawable.profpiclogo);
        }
        else if (propic.equals("Female")){
            ProPic.setImageResource(R.drawable.femaleprofpic);
        }
        else{
            Glide.with(this).load(userDetails.getProfPic()).into(ProPic);
        }

        UserName.setText(""+userDetails.getName());
        Address.setText(""+userDetails.getCity()+", "+userDetails.getState());

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri number = Uri.parse("tel:"+userDetails.getMobNo());
                Intent intent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(intent);
            }
        });

        /*Call2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri number = Uri.parse("tel:"+userDetails.getAltMobNo());
                Intent intent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(intent);
            }
        });*/

        Sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", userDetails.getMobNo(), null)));
            }
        });

        Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("mailto", userDetails.getEmail(), null)));
            }
        });

        WhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=+91"+userDetails.getMobNo());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        /*ConfirmHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmHelp.setText("Thanks superhero, keep helping!");
                ConfirmHelp.setBackgroundTintList(getResources().getColorStateList(R.color.green));
                ConfirmHelp.setEnabled(false);
            }
        });*/
    }
}
