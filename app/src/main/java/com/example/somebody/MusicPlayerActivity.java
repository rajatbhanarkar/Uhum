package com.example.somebody;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.savantech.seekarc.SeekArc;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    ImageView PlayPause;
    SeekArc seekArc;
    boolean isPlaying = false;
    String url = "https://storage.googleapis.com/caronaapp/Music%20For%20App/MEDITATION/ADVANCE%20LEVEL/FLUTE/FLUTE%20-%20%2050%20mins.mp3";
    int currtime = 0;
    SeekBar VolumeBar;
    MediaPlayer mediaPlayer;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        getWindow().setStatusBarColor(Color.parseColor("#feccb5"));

        PlayPause = (ImageView)findViewById(R.id.ivplaypause);
        seekArc = (SeekArc)findViewById(R.id.samusicplayer);
        VolumeBar = (SeekBar)findViewById(R.id.sbvolume);

        seekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {

            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {

            }

            @Override
            public void onProgressChanged(SeekArc seekArc, float progress) {
                mediaPlayer.seekTo((int)progress);
            }
        });

        PlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!isPlaying){
                        progressDialog = new ProgressDialog(MusicPlayerActivity.this);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();

                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(url);
                        mediaPlayer.prepareAsync();

                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(final MediaPlayer mediaPlayer) {
                                progressDialog.dismiss();
                                seekArc.setMaxProgress(mediaPlayer.getDuration());
                                mediaPlayer.start();
                                isPlaying = true;
                                PlayPause.setImageResource(R.drawable.ic_pause);

                                new Timer().scheduleAtFixedRate(new TimerTask() {
                                    @Override
                                    public void run() {
                                        currtime = mediaPlayer.getCurrentPosition();
                                        seekArc.setProgress(currtime);
                                    }
                                },0,100);
                            }
                        });
                    }
                    else{
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
}
