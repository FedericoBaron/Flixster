package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.flixster.databinding.ActivityMovieTrailerBinding;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    public static final String TAG = "MovieTrailerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMovieTrailerBinding binding = ActivityMovieTrailerBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        callYoutubeAPI(binding);

    }

    private void callYoutubeAPI(ActivityMovieTrailerBinding binding){

        // The key for the video that needs to be played using Youtube API
        final String videoKey = getIntent().getStringExtra(MovieDetailsActivity.KEY_LINK);

        // Resolve the player view from the layout
        YouTubePlayerView playerView = binding.player;


        // Initialize with API key stored in secrets.xml
        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                // Do any work here to cue video, play video, etc.
                Log.d(TAG, "onSuccess");
                Log.d(TAG, "THE VIDEO ID IS " + videoKey);
                youTubePlayer.loadVideo(videoKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                // Log the error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });
    }

}