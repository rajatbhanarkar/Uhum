package com.example.somebody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainPageActivity extends AppCompatActivity {

    TextView Confirmed, Active, Recovered, Deceased;
    CardView cardView1, cardView2, cardView3, cardView4, cardView5, cardView6;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Confirmed = (TextView)findViewById(R.id.tvhpaconfirmed);
        Active = (TextView)findViewById(R.id.tvhpaactive);
        Recovered = (TextView)findViewById(R.id.tvhparecovered);
        Deceased = (TextView)findViewById(R.id.tvhpadeceased);

        cardView1 = (CardView)findViewById(R.id.cvhpacard1);
        cardView2 = (CardView)findViewById(R.id.cvhpacard2);
        cardView3 = (CardView)findViewById(R.id.cvhpacard3);
        cardView4 = (CardView)findViewById(R.id.cvhpacard4);
        cardView5 = (CardView)findViewById(R.id.cvhpacard5);
        cardView6 = (CardView)findViewById(R.id.cvhpacard6);

        requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.covid19india.org/data.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONArray("statewise").getJSONObject(0);
                    Confirmed.setText(""+jsonObject.getString("confirmed"));
                    Active.setText(""+jsonObject.getString("active"));
                    Recovered.setText(""+jsonObject.getString("recovered"));
                    Deceased.setText(""+jsonObject.getString("deaths"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HelpSomeoneActivity.class);
                startActivity(intent);
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GetHelpActivity.class);
                startActivity(intent);
            }
        });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
                startActivity(intent);
            }
        });

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GetHelpActivity.class);
                startActivity(intent);
            }
        });

        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FAQActivity.class);
                startActivity(intent);
            }
        });

        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
            }
        });

    }
}
