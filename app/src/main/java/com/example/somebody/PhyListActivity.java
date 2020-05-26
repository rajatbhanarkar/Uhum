package com.example.somebody;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PhyListActivity extends AppCompatActivity {

    BufferedReader reader = null;
    String jsonString = "";
    int category = 0;
    String CatName = "";

    ArrayList<SongDetails> SongList = new ArrayList<>();
    int[] Images = {R.drawable.aerobicexlogo, R.drawable.workoutlogo, R.drawable.stretexlogo, R.drawable.balexlogo, R.drawable.yogaexlogo};
    int currImage;
    int[] colors = {R.color.lightSkin, R.color.periwinkle, R.color.palePink, R.color.skinColor};

    ListView medList;
    ImageView Back;
    TextView CategoryTitle;

    String[] vidname = {"Vrikshasana (Hindi)", "Vrikshasana (English)", "Tadasana (Hindi)", "Tadasana (English)", "Trikonasana (Hindi)", "Trikonasana (English)", "Ardha Chakrasana (Hindi)", "Ardha Chakrasana (English)", "Padahastasana (Hindi)", "Padahastasana (English)", "Bhadrasan (Hindi)", "Bhadrasan (English)", "Ustrasana (Hindi)", "Ustrasana (English)", "Vajrasana (Hindi)", "Vajrasana (English)", "Shashankasana (Hindi)", "Shashankasana (English)", "Vakrasana (Hindi)", "Vakrasana (English)", "Bhujangasana (Hindi)", "Bhujangasana (English)", "Shalabhasana (Hindi)", "Shalabhasana (English)", "Pawanmuktasana (Hindi)", "Pawanmuktasana (English)", "Setubandhasana (Hindi)", "Setubandhasana (English)", "Nadi Shodhan Pranayam (Hindi)", "Nadi Shodhan Pranayam (English)", "Dhyana (Hindi)", "Dhyana (English)", "Suryanamaskar (Hindi)", "Suryanamaskar (English)"};
    String[] vidid = {"fIF016JROiA", "QeYhMXJWpJg", "QM9x8ZpaYPc", "drBqFWcLEcE", "misHjEvOskI", "FSdVBFpT6i4", "i_ix1f99Vn4", "i6EPVHHlFNk", "_oM_OGcaSRQ", "qqhx3bckcjs", "X-oPQ9eO360", "9WDAW2yA2Og", "JDzDfraiR7U", "dwVMKOmHAXY", "GDwDN0DUNm8", "tFIrxireDAo", "i7460Bqvz3Q", "9FL5WlTXo-0", "HYq5Ao1LOAk", "RY2nFv743-A", "-HgeZztTSec", "99RR2vRvgi8", "dkCiQuLwI1U", "mJv68rV86j8", "6w4chpJaQl4", "tKS0OO58IZQ", "xIMRYcA7TkA", "Exyjk7hNs4c", "TSvxys_Ywq0", "vLJcIibEQwU", "-73jLhEosSQ", "YPFEOUZTT8", "riURtZa6MhU", "oHGuvXPBtEc"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phy_list);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        category = getIntent().getIntExtra("Category", 1);
        CatName = getIntent().getStringExtra("CatName");

        medList = (ListView)findViewById(R.id.lvmedlist);
        Back = (ImageView)findViewById(R.id.ivmlaback);
        CategoryTitle = (TextView)findViewById(R.id.tvmlacatname);

        CategoryTitle.setText(""+CatName);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("phylist.json")));

            String line;
            while ((line = reader.readLine()) != null) {
                jsonString += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            JSONObject mainObject = new JSONObject(jsonString).getJSONObject("meditation");
            JSONArray catArray = null;
            currImage = Images[category-1];

            switch (category){
                case 1: catArray = mainObject.getJSONArray("basic");  break;
                case 2: catArray = mainObject.getJSONArray("guided"); break;
                case 3: catArray = mainObject.getJSONArray("mantra"); break;
                case 4: catArray = mainObject.getJSONArray("immunityBooster"); break;
                case 5: catArray = mainObject.getJSONArray("advance"); break;
            }

            for (int i=0 ; i<catArray.length() ; i++){
                String songUrl = catArray.getJSONObject(i).getString("link");
                String songName = URLUtil.guessFileName(songUrl, null, null);
                songName = songName.replace(".bin", "");
                songName = songName.replace(".mp3", "");

                SongDetails songDetails = new SongDetails();
                songDetails.setSongURL(songUrl);
                songDetails.setSongName(songName);

                SongList.add(songDetails);
            }

            MedListCustomAdapter medListCustomAdapter = new MedListCustomAdapter();
            medList.setAdapter(medListCustomAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MedListCustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return vidname.length;
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
            view = getLayoutInflater().inflate(R.layout.med_list_layout, null);
            LinearLayout layout = (LinearLayout)view.findViewById(R.id.llmedmain);
            ImageView iv = (ImageView)view.findViewById(R.id.ivmllimage);
            TextView tv = (TextView)view.findViewById(R.id.tvmlltitle);

            tv.setText(vidname[i]);
            layout.setBackgroundTintList(getResources().getColorStateList(colors[i%4]));
            iv.setImageResource(currImage);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (category == 5){
                        Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                        intent.putExtra("VidName", vidname[i]);
                        intent.putExtra("VidID", vidid[i]);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), GifPlayerActivity.class);
                        startActivity(intent);
                    }
                    /*intent.putExtra("SongDetails", SongList.get(i));
                    intent.putExtra("CatName", CatName);
                    intent.putExtra("CurrImage", currImage);*/
                }
            });

            return view;
        }
    }
}
