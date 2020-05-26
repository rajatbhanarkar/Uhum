package com.example.somebody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HelpSomeoneActivity extends AppCompatActivity {

    String[] colors = {"#feeed5","#d3d7fe","#fae4e6","#feccb5"};

    ListView listView;

    CardView Food, Medical, Financial, Shelter, Other;
    Button Search;
    ImageView Back;
    boolean sel1 = false, sel2 = false, sel3 = false, sel4 = false, sel5 = false;

    ArrayList<HelpDetails> HelpList = new ArrayList<>();

    String category = "";

    DatabaseReference myRef;
    HelpDetails helpDetails;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_someone);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        listView = (ListView)findViewById(R.id.lvhelpsomeone);
        Food = (CardView)findViewById(R.id.cvhsacard1);
        Medical = (CardView)findViewById(R.id.cvhsacard2);
        Financial = (CardView)findViewById(R.id.cvhsacard3);
        Shelter = (CardView)findViewById(R.id.cvhsacard4);
        Other = (CardView)findViewById(R.id.cvhsacard5);
        Search = (Button) findViewById(R.id.btnhsasearch);
        Back = (ImageView)findViewById(R.id.ivghaback);

        myRef = FirebaseDatabase.getInstance().getReference();

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
                category += (sel1)?("Food "):("");
                category += (sel2)?("Medical "):("");
                category += (sel3)?("Financial "):("");
                category += (sel4)?("Shelter "):("");
                category += (sel5)?("Other "):("");

                if (category.equals("")){
                    Toast.makeText(HelpSomeoneActivity.this, "Please select a category!", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog = new ProgressDialog(HelpSomeoneActivity.this);
                dialog.setMessage("Searching...");
                dialog.setCancelable(false);
                dialog.show();

                Query query = myRef.child("AskForHelp");

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                helpDetails = snapshot.getValue(HelpDetails.class);
                                HelpList.add(helpDetails);
                            }
                            HelpSomeoneCustomAdapter customAdapter = new HelpSomeoneCustomAdapter();
                            listView.setAdapter(customAdapter);
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(HelpSomeoneActivity.this, "No Results Found!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    class HelpSomeoneCustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return HelpList.size();
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

            LinearLayout layout = (LinearLayout) view.findViewById(R.id.llplmain);
            TextView name = (TextView) view.findViewById(R.id.tvplname);
            TextView address = (TextView) view.findViewById(R.id.tvpllocation);
            TextView description = (TextView) view.findViewById(R.id.tvpldesc);
            ImageView UserPic = (ImageView)view.findViewById(R.id.ivplpropic);
            ImageView HelpPic = (ImageView)view.findViewById(R.id.ivplhelppic);

            Glide.with(HelpSomeoneActivity.this).load(HelpList.get(i).getUserPicLink()).into(UserPic);
            Glide.with(HelpSomeoneActivity.this).load(HelpList.get(i).getHelpPicLink()).into(HelpPic);

            //layout.setBackgroundColor(Color.parseColor(colors[i % 4]));
            name.setText(HelpList.get(i).getUserName());
            address.setText(HelpList.get(i).getHelpAddress());
            description.setText(HelpList.get(i).getDescription());

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), DisplayProfileActivity.class);
                    startActivity(intent);
                }
            });

            return view;
        }
    }

    void select(RelativeLayout view){
        view.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        TextView tv = (TextView)view.getChildAt(1);
        tv.setTextColor(Color.parseColor("#ffffff"));
    }

    void deselect(RelativeLayout view){
        view.setBackgroundTintList(null);
        TextView tv = (TextView)view.getChildAt(1);
        tv.setTextColor(Color.parseColor("#000000"));
    }
}
