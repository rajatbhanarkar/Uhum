package com.example.somebody;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

public class GifPlayerActivity extends AppCompatActivity {

    String url = "https://www.cdn.spotebi.com/wp-content/uploads/2014/10/squat-exercise-illustration.gif";
    ImageView Gif, Back, Prev, Next;
    Button Start;
    TextView Desc;
    TextToSpeech textToSpeech = null;
    int count = 0;
    String s = "Let's do Squats. Press Next and Previous buttons to navigate instructions.\n1) Stand with feet a little wider than hip width, toes facing front.\n2) Drive your hips back—bending at the knees and ankles and pressing your knees slightly open.\n3) Sit into a squat position while still keeping your heels and toes on the ground, chest up and shoulders back.\n4) Strive to eventually reach parallel, meaning knees are bent to a 90-degree angle.\n5) Press into your heels and straighten legs to return to a standing upright position.\nThis is one squat. Repeat this exercise several times";
    String[] ss = s.split("\n");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_player);

        try {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            Gif = (ImageView) findViewById(R.id.ivgifplayer);
            Back = (ImageView) findViewById(R.id.ivmhaback);
            Prev = (ImageView) findViewById(R.id.ivgpaprev);
            Next = (ImageView) findViewById(R.id.ivgpanext);
            Start = (Button) findViewById(R.id.btngpastart);
            Desc = (TextView) findViewById(R.id.tvgpadesc);

            Glide.with(this).asGif().load(url).into(Gif);

            Back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            Start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    count = 0;
                    textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int i) {
                            if (i == TextToSpeech.SUCCESS) {
                                int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                                int speech = textToSpeech.speak(ss[count], TextToSpeech.QUEUE_FLUSH, null);
                                Desc.setText("" + ss[count]);
                            }
                        }
                    });
                }
            });

            Prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (count > 1 && textToSpeech != null) {
                        count--;
                        int speech = textToSpeech.speak(ss[count], TextToSpeech.QUEUE_FLUSH, null);
                        Desc.setText("" + ss[count]);
                    }
                }
            });

            Next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (count < ss.length - 1 && textToSpeech != null) {
                        count++;
                        int speech = textToSpeech.speak(ss[count], TextToSpeech.QUEUE_FLUSH, null);
                        Desc.setText("" + ss[count]);
                    }
                }
            });

        }catch (Exception e){
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter, true);
            e.printStackTrace(printWriter);

            Dialog dialog = new Dialog(GifPlayerActivity.this, R.style.myDialog);
            View viewww = getLayoutInflater().inflate(R.layout.exep_disp_layout, null);
            TextView tv = (TextView)viewww.findViewById(R.id.tvexep);
            tv.setText(""+stringWriter.getBuffer().toString());
            dialog.setContentView(viewww);
            dialog.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (textToSpeech != null){
            textToSpeech.shutdown();
        }
        Desc.setText("");
    }
}
