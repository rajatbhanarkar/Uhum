package com.example.somebody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class HelpSomeoneActivity extends AppCompatActivity {

    String[] colors = {"#feeed5","#d3d7fe","#fae4e6","#feccb5"};

    ListView listView;

    CardView Food, Medical, Financial, Shelter, Other;
    TextView Search;
    boolean sel1 = false, sel2 = false, sel3 = false, sel4 = false, sel5 = false;

    ArrayList<String> cat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_someone);

        listView = (ListView)findViewById(R.id.lvhelpsomeone);
        Food = (CardView)findViewById(R.id.cvhsacard1);
        Medical = (CardView)findViewById(R.id.cvhsacard2);
        Financial = (CardView)findViewById(R.id.cvhsacard3);
        Shelter = (CardView)findViewById(R.id.cvhsacard4);
        Other = (CardView)findViewById(R.id.cvhsacard5);
        Search = (TextView)findViewById(R.id.tvhsasearch);

        Food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sel1){ deselect((RelativeLayout) Food.getChildAt(0)); sel1 = false; }
                else{ select((RelativeLayout) Food.getChildAt(0)); sel1 = true; }
            }
        });

        Medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sel2){ deselect((RelativeLayout) Medical.getChildAt(0)); sel2 = false; }
                else{ select((RelativeLayout) Medical.getChildAt(0)); sel2 = true; }
            }
        });

        Financial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sel3){ deselect((RelativeLayout) Financial.getChildAt(0)); sel3 = false; }
                else{ select((RelativeLayout) Financial.getChildAt(0)); sel3 = true; }
            }
        });

        Shelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sel4){ deselect((RelativeLayout) Shelter.getChildAt(0)); sel4 = false; }
                else{ select((RelativeLayout) Shelter.getChildAt(0)); sel4 = true; }
            }
        });

        Other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sel5){ deselect((RelativeLayout) Other.getChildAt(0)); sel5 = false; }
                else{ select((RelativeLayout) Other.getChildAt(0)); sel5 = true; }
            }
        });

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (sel1){ cat.add("Food"); } if (sel2){ cat.add("Medical"); } if (sel3){ cat.add("Financial"); } if (sel4){ cat.add("Other"); }

                HelpSomeoneCustomAdapter customAdapter = new HelpSomeoneCustomAdapter();
                listView.setAdapter(customAdapter);
            }
        });

    }

    class HelpSomeoneCustomAdapter extends BaseAdapter {

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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.post_layout, null);

            /*RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.rlnmdl);
            TextView name = (TextView) view.findViewById(R.id.tvnmdlname);
            TextView description = (TextView) view.findViewById(R.id.tvnmdldescription);
            ImageView Call = (ImageView) view.findViewById(R.id.ivcall);
            ImageView Email = (ImageView) view.findViewById(R.id.ivemail);
            ImageView Chat = (ImageView) view.findViewById(R.id.ivchat);

            layout.setBackgroundColor(Color.parseColor(colors[i % 4]));
            name.setText(HelpList.get(i).getName());
            description.setText(HelpList.get(i).getDescription());

            Call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri number = Uri.parse("tel:" + HelpList.get(i).getPhoneNo());
                    Intent intent = new Intent(Intent.ACTION_DIAL, number);
                    startActivity(intent);
                }
            });

            Email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("mailto", HelpList.get(i).getEmail(), null)));
                }
            });

            Chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", HelpList.get(i).getPhoneNo(), null)));
                }
            });*/

            return view;
        }
    }

    void select(RelativeLayout view){
        view.setBackgroundTintList(getResources().getColorStateList(R.color.darkcerulian));
        TextView tv = (TextView)view.getChildAt(1);
        tv.setTextColor(Color.parseColor("#ffffff"));
    }

    void deselect(RelativeLayout view){
        view.setBackgroundTintList(null);
        TextView tv = (TextView)view.getChildAt(1);
        tv.setTextColor(Color.parseColor("#000000"));
    }
}
