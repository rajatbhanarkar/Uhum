package com.example.somebody;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.savantech.seekarc.SeekArc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.util.DisplayMetrics;

public class MusicPlayerActivity extends AppCompatActivity {

    ImageView PlayPause, Previous, Next;
    SeekArc seekArc;
    boolean isPlaying = false;
    int currtime = 0, count = 0, totalTime = 0;
    MediaPlayer mediaPlayer;

    SongDetails songDetails;
    TextView CategoryTitle, SongName, TimeDisplay;
    ImageView CatPic, Back;

    Dialog LoaderDialog;
    boolean flag = false;

    ArrayList<SongDetails> SongList = new ArrayList<>();
    int index = 0;

    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_music_player);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setWindowAnimations(0);


        SongList = (ArrayList<SongDetails>) getIntent().getSerializableExtra("SongList");
        index = getIntent().getIntExtra("Selected", 0);

        songDetails = SongList.get(index);

        PlayPause = (ImageView) findViewById(R.id.ivplaypause);
        Previous = (ImageView) findViewById(R.id.ivmpaprevious);
        Next = (ImageView) findViewById(R.id.ivmpanext);
        seekArc = (SeekArc) findViewById(R.id.samusicplayer);
        CategoryTitle = (TextView) findViewById(R.id.tvmpacatname);
        SongName = (TextView) findViewById(R.id.tvmpasongname);
        TimeDisplay = (TextView) findViewById(R.id.tvmpatime);
        CatPic = (ImageView) findViewById(R.id.ivmpacatpic);
        Back = (ImageView) findViewById(R.id.ivmpaback);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index == 0) {
                    index = SongList.size();
                }
                Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
                intent.putExtra("SongList", SongList);
                intent.putExtra("Selected", index - 1);
                intent.putExtra("CatName", getIntent().getStringExtra("CatName"));
                intent.putExtra("CurrImage", getIntent().getIntExtra("CurrImage", 0));
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index == SongList.size() - 1) {
                    index = -1;
                }
                Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
                intent.putExtra("SongList", SongList);
                intent.putExtra("Selected", index + 1);
                intent.putExtra("CatName", getIntent().getStringExtra("CatName"));
                intent.putExtra("CurrImage", getIntent().getIntExtra("CurrImage", 0));
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });

        seekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {

            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {

            }

            @Override
            public void onProgressChanged(SeekArc seekArc, float progress) {
                mediaPlayer.seekTo((int) progress);
            }
        });

        CategoryTitle.setText("" + getIntent().getStringExtra("CatName"));
        SongName.setText("" + songDetails.getSongName());
        CatPic.setImageResource(getIntent().getIntExtra("CurrImage", 0));

        mediaPlayer = new MediaPlayer();

        PlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!isPlaying) {
                        if (count == 0) {
                            LoaderDialog = new Dialog(MusicPlayerActivity.this);
                            View vieww = getLayoutInflater().inflate(R.layout.loader_layout, null);
                            ProgressBar progressBar = (ProgressBar) vieww.findViewById(R.id.spinKit);
                            DoubleBounce doubleBounce = new DoubleBounce();
                            progressBar.setIndeterminateDrawable(doubleBounce);
                            LoaderDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            LoaderDialog.setContentView(vieww);
                            LoaderDialog.show();

                            mediaPlayer.setDataSource("https://storage.googleapis.com/caronaapp/music_uman/Emotion%20-%202.15%20mins.mp3");
                            mediaPlayer.prepareAsync();

                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(final MediaPlayer mediaPlayer) {
                                    LoaderDialog.dismiss();
                                    totalTime = mediaPlayer.getDuration();
                                    seekArc.setMaxProgress(totalTime);
                                    mediaPlayer.setLooping(true);
                                    mediaPlayer.start();
                                    isPlaying = true;
                                    PlayPause.setImageResource(R.drawable.ic_pause);
                                }
                            });

                            count++;
                        } else {
                            mediaPlayer.start();
                            isPlaying = true;
                            PlayPause.setImageResource(R.drawable.ic_pause);
                        }


                        Handler handler = new Handler();
                        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mediaPlayer != null){
                                    currtime = mediaPlayer.getCurrentPosition();
                                    seekArc.setProgress(currtime);
                                    String time = String.format("%02d:%02d / %02d:%02d", TimeUnit.MILLISECONDS.toMinutes(currtime), TimeUnit.MILLISECONDS.toSeconds(currtime)%60, TimeUnit.MILLISECONDS.toMinutes(totalTime), TimeUnit.MILLISECONDS.toSeconds(totalTime)%60);
                                    TimeDisplay.setText(""+time);
                                }
                                handler.postDelayed(this, 1000);
                            }
                        });
                    } else {
                        mediaPlayer.pause();
                        isPlaying = false;
                        PlayPause.setImageResource(R.drawable.ic_play);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
        isPlaying = false;
        PlayPause.setImageResource(R.drawable.ic_play);
    }
}
