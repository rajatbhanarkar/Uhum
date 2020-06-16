package com.example.somebody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainPageActivity extends AppCompatActivity {

    TextView Confirmed, Active, Recovered, Deceased;
    CardView cardView1, cardView2, cardView3, cardView4, cardView5, cardView6;
    RequestQueue requestQueue;

    UserDetails userDetails;

    ImageView Propic;
    TextView Greeting;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    LinearLayout linearLayout;
    DrawerLayout drawerLayout;
    ImageView MenuBack, Menu;
    ListView MenuItems;
    CircleImageView MenuProPic;
    TextView MenuName, MenuLocation;

    String[] menuTitles = { "View Profile", "Edit Profile", "Contact Us", "FeedBack", "Log Out" };
    int[] menuIcons = { R.drawable.profpiclogo, R.drawable.editprofile, R.drawable.supportlogo, R.drawable.feedbacklogo, R.drawable.logoutlogo };

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

        linearLayout = (LinearLayout)findViewById(R.id.llrightdrawer);
        drawerLayout = (DrawerLayout)findViewById(R.id.dlmain);
        Menu = (ImageView)findViewById(R.id.ivhpamenu);
        MenuBack = (ImageView)findViewById(R.id.ivmenuback);
        MenuItems = (ListView)findViewById(R.id.lvsidemenu);
        MenuProPic = (CircleImageView)findViewById(R.id.ivmenupropic);
        MenuName = (TextView)findViewById(R.id.tvmenuname);
        MenuLocation = (TextView)findViewById(R.id.tvmenudetails);

        Propic = (ImageView)findViewById(R.id.ivmpapropic);
        Greeting = (TextView)findViewById(R.id.tvgreeting);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        sharedPreferences = getSharedPreferences("SomebodySharedPreferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userDetails = new Gson().fromJson(sharedPreferences.getString("UserDetails",""), UserDetails.class);

        String propic = userDetails.getProfPic();

        Dialog dialog = new Dialog(this, R.style.myDialog);
        dialog.setContentView(R.layout.avatar_select_dialog);
        dialog.show();

        if (propic.equals("Male")){
            Propic.setImageResource(R.drawable.profpiclogo);
            MenuProPic.setImageResource(R.drawable.profpiclogo);
        }
        else if (propic.equals("Female")){
            Propic.setImageResource(R.drawable.femaleprofpic);
            MenuProPic.setImageResource(R.drawable.femaleprofpic);
        }
        else{
            Glide.with(this).load(userDetails.getProfPic()).into(Propic);
            Glide.with(this).load(userDetails.getProfPic()).into(MenuProPic);
        }

        Greeting.setText("Hi "+userDetails.getName().split(" ")[0]+"!");

        MenuName.setText(userDetails.getName());
        MenuLocation.setText(""+userDetails.getCity()+", "+userDetails.getState());

        MenuCustomadapter menuCustomadapter = new MenuCustomadapter();
        MenuItems.setAdapter(menuCustomadapter);

        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(linearLayout);
            }
        });

        MenuBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(linearLayout);
            }
        });

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
                Intent intent = new Intent(getApplicationContext(), MentalHealthActivity.class);
                startActivity(intent);
            }
        });

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PhysicalHealthActivity.class);
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

    class MenuCustomadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return menuTitles.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.drawer_menu_layout, null);
            LinearLayout ll = (LinearLayout)view.findViewById(R.id.lldmlmain);
            ImageView iv = (ImageView)view.findViewById(R.id.ivdmllogo);
            TextView tv = (TextView)view.findViewById(R.id.tvdmltitle);
            iv.setImageResource(menuIcons[i]);
            tv.setText(menuTitles[i]);

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.closeDrawer(linearLayout);
                    switch (i){
                        case 0:
                            Intent intent1 = new Intent(getApplicationContext(), DisplayProfileActivity.class);
                            intent1.putExtra("UserDetails", userDetails);
                            startActivity(intent1);
                            break;
                        case 1:
                            Intent intent2 = new Intent(getApplicationContext(), EditProfileActivity.class);
                            startActivity(intent2);
                            break;
                        case 2:
                            Intent intent3 = new Intent(getApplicationContext(), WebViewActivity.class);
                            intent3.putExtra("URL", "http://chat.renovatio.procohat.tech/");
                            startActivity(intent3);
                            break;
                        case 3:
                            Intent intent4 = new Intent(getApplicationContext(), WebViewActivity.class);
                            intent4.putExtra("URL", "https://docs.google.com/forms/d/e/1FAIpQLScQvJd_LzjXO_k5N7LXtcaRSvbAA8xMPHF6CUKnT6KKLCikSQ/viewform");
                            startActivity(intent4);
                            break;
                        case 4:
                            editor.clear(); editor.commit(); editor.apply();
                            Intent intent5 = new Intent(getApplicationContext(), EnterNumberActivity.class);
                            intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent5);
                            break;
                    }
                }
            });
            return view;
        }
    }
}
