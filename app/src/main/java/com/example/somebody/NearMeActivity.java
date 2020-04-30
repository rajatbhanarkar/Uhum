package com.example.somebody;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class NearMeActivity extends AppCompatActivity {

    ListView NearMe;
    ImageView Search;
    TextView NearMeTitle;
    String[] colors = {"#feeed5","#d3d7fe","#fae4e6","#feccb5"};

    ArrayList<String> Names = new ArrayList<>(Arrays.asList(new String[]{"Aditya Hospital", "Saxena Hospital"}));
    ArrayList<String> Address = new ArrayList<>(Arrays.asList(new String[]{"Napier Town, Jabalpur", "Wright Town, Jabalpur"}));
    ArrayList<String> Distance = new ArrayList<>(Arrays.asList(new String[]{"2.3KM Away", "3.5KM Away"}));

    ImageView Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);

        NearMe = (ListView)findViewById(R.id.lvnearme);
        Search = (ImageView)findViewById(R.id.ivsearch);
        NearMeTitle = (TextView)findViewById(R.id.tvnearmetitle);
        Back = (ImageView)findViewById(R.id.ivback);

        if (getIntent().getIntExtra("Type", 1) != 1){
            NearMeTitle.setText("Isolation Wards Near Me");
        }

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAdapter customAdapter = new CustomAdapter();
                NearMe.setAdapter(customAdapter);
                NearMe.setSmoothScrollbarEnabled(true);
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.near_me_display_layout, null);

            RelativeLayout layout = (RelativeLayout)view.findViewById(R.id.rlnmdl);
            TextView name = (TextView)view.findViewById(R.id.tvnmdlname);
            TextView address = (TextView)view.findViewById(R.id.tvnmdladdress);
            TextView distance = (TextView)view.findViewById(R.id.tvnmdldistance);

            layout.setBackgroundColor(Color.parseColor(colors[i%4]));
            name.setText(Names.get(0));
            address.setText(Address.get(0));
            distance.setText(Distance.get(0));

            return view;
        }
    }
}
