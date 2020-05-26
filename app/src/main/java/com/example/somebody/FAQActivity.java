package com.example.somebody;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FAQActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> Questions = new ArrayList<>();
    ArrayList<String> Answers = new ArrayList<>();
    BufferedReader reader = null;
    String question = "", answer = "";
    int count = 0;
    ImageView Back;

    int[] colors = {R.color.lightSkin, R.color.periwinkle, R.color.yellow2, R.color.palePink, R.color.skinColor,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_a_q);

        listView = (ListView)findViewById(R.id.lvfaqlist);
        Back = (ImageView)findViewById(R.id.ivback);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("coronafaq")));

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals("")){
                    if (count%2 == 0){ question += line; }
                    else{ answer += line; }
                }
                else{
                    if (count%2 == 0){ Questions.add(question); }
                    else{ Answers.add(answer); }
                    question = ""; answer = "";
                    count++;
                }
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

        FAQCustomAdapter faqCustomAdapter = new FAQCustomAdapter();
        listView.setAdapter(faqCustomAdapter);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    class FAQCustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Questions.size();
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
            view = getLayoutInflater().inflate(R.layout.faq_layout, null);

            TextView Ques = (TextView)view.findViewById(R.id.tvfaqlquestion);
            TextView Ans = (TextView)view.findViewById(R.id.tvfaqlanswer);

            Ques.setText(Questions.get(i));
            Ans.setText(Answers.get(i));

            return view;
        }
    }
}
