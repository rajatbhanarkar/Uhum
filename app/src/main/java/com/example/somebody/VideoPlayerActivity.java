package com.example.somebody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoPlayerActivity extends AppCompatActivity {

    String VidName, VidID;
    TextView Name;
    ImageView Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        Back = (ImageView)findViewById(R.id.ivmhaback);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        VidName = getIntent().getStringExtra("VidName");
        VidID = getIntent().getStringExtra("VidID");

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        Name = (TextView)findViewById(R.id.tvvpaname);
        getLifecycle().addObserver(youTubePlayerView);
        Name.setText(""+VidName);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = VidID;
                youTubePlayer.loadVideo(videoId, 0);
            }
        });
    }
}
