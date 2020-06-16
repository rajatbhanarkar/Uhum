package com.example.somebody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class WalkThroughActivity extends AppCompatActivity {

    private ViewPager viewPager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private SliderAdapter sliderAdapter;
    int currentpage = 0;

    int[] colors1 = {R.color.skinColor, R.color.yellow2, R.color.periwinkle, R.color.lightSkin, R.color.palePink, R.color.skinColor, R.color.yellow2};
    int[] colors2 = {R.color.periwinkle, R.color.lightSkin, R.color.palePink, R.color.skinColor, R.color.yellow2, R.color.periwinkle, R.color.lightSkin};
    int[] colors3 = {R.color.palePink, R.color.skinColor, R.color.yellow2, R.color.periwinkle, R.color.lightSkin, R.color.palePink, R.color.skinColor};
    int[] colors4 = {R.color.lightSkin, R.color.palePink, R.color.skinColor, R.color.yellow2, R.color.periwinkle, R.color.lightSkin, R.color.palePink};
    int[] colors5 = {R.color.yellow2, R.color.periwinkle, R.color.lightSkin, R.color.palePink, R.color.skinColor, R.color.yellow2, R.color.periwinkle};

    String[] names = {"help_someone_anim.json", "get_help_anim.json", "meditation_anim.json", "workout_anim.json", "counselling_anim.json", "faq_anim.json", "covid_anim.json"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through);

        viewPager = (ViewPager)findViewById(R.id.viewPager);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        sharedPreferences = getSharedPreferences("SomebodySharedPreferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        viewPager.addOnPageChangeListener(viewListener);
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentpage = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public class SliderAdapter extends PagerAdapter {

        LayoutInflater layoutInflater;
        Context context;

        int[] Images = {R.drawable.helpsomeone, R.drawable.helplogo, R.drawable.meditationlogo, R.drawable.workoutlogo, R.drawable.counsellorlogo, R.drawable.faq, R.drawable.coronalogo};
        String[] MainTitles = {"Be a superhero! Help people in these tough times!", "Need something? Get help from thousands of our volunteers!", "Stressed out? Feeling low? Meditate to calm yourself down!", "Workout with us and keep your body fit in this quarantine!", "Confused? Have Questions? Our counsellors are here to help you 24x7!", "Wanna know more? Check out the FAQs and latest trends of Covid-19!", "Know more about Covid-19 and its symptoms!"};

        public SliderAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == (RelativeLayout) object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.walkthrough_slider_layout, container, false);

            TextView MainHeadings = (TextView) view.findViewById(R.id.tvwtslmain);
            LottieAnimationView lav = (LottieAnimationView) view.findViewById(R.id.lavwtslimage);
            CardView Next = (CardView) view.findViewById(R.id.cvwtanext);
            ImageView iv1 = (ImageView)view.findViewById(R.id.iv1);
            ImageView iv2 = (ImageView)view.findViewById(R.id.iv2);
            ImageView iv3 = (ImageView)view.findViewById(R.id.iv3);
            ImageView iv4 = (ImageView)view.findViewById(R.id.iv4);
            //ImageView iv5 = (ImageView)view.findViewById(R.id.iv5);

            iv1.setBackgroundColor(getResources().getColor(colors1[position]));
            iv2.setBackgroundColor(getResources().getColor(colors2[position]));
            iv3.setBackgroundColor(getResources().getColor(colors3[position]));
            iv4.setBackgroundColor(getResources().getColor(colors4[position]));
            //iv5.setBackgroundColor(getResources().getColor(colors5[position]));

            lav.setAnimation(names[position]);

            MainHeadings.setText(MainTitles[position]);

            Next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentpage == 6){
                        editor.putInt("FirstTime", 1);
                        editor.apply(); editor.commit();

                        Intent intent = new Intent(getApplicationContext(), EnterNumberActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    viewPager.setCurrentItem(currentpage+1);
                }
            });

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((RelativeLayout) object);
        }
    }
}
