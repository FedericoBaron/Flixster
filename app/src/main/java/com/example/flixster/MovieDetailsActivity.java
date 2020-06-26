package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String KEY_LINK = "link";
    public static final String TAG = "MovieDetailsActivity";

    // The movie to display
    Movie movie;

    String videoId;

    // the view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    TextView popScore;
    ImageView thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Resolve the view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        popScore = (TextView) findViewById(R.id.popularityScore);
        thumbnail = (ImageView) findViewById(R.id.thumbnail);

        // Unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));

        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie));

        wireUI();
        String link = callMovieAPI();

    }

    private void wireUI()
    {
        // Set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // Set popularity score
        double score = movie.getPopularity();
        popScore.setText(String.valueOf(score));

        // Vote avg is 0...10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue()/2;
        rbVoteAverage.setRating(voteAverage);

        String imageUrl = movie.getBackdropPath();
        int placeholder = placeholder = R.drawable.flicks_backdrop_placeholder;
        // Binds image to ViewHolder with rounded corners
        int radius = 18; // corner radius, higher value = more rounded
        int margin = 0; // crop margin, set to 0 for corners with no crop
        Glide.with(this)
                .load(imageUrl)
                .placeholder(placeholder)
                .fitCenter()
                .transform(new RoundedCornersTransformation(radius, margin))
                .into(thumbnail);
    }

    private void playVideoListener(final String link) {
        // Listener for thumbnail click
        thumbnail.setOnClickListener(new View.OnClickListener() {
            // Handler for add button click
            @Override
            public void onClick(View v) {

                Log.i(TAG, "THE LINK FOR MOVIE IS: " + link);

                // Create the new activity
                Intent i = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);

                // Add link to intent
                i.putExtra(KEY_LINK, link);

                // Display the activity
                startActivity(i);
            }
        });
    }

    private String callMovieAPI(){
        int movieId = movie.getId();

        // Gets the api key from secret XML and puts it in URL
        String movies_key = getString(R.string.movies_api_key);
        final String URL = "https://api.themoviedb.org/3/movie/" + Integer.toString(movieId) + "/videos?api_key=" + movies_key;
        Log.i(TAG, "URL IS:" + URL);

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(URL, new JsonHttpResponseHandler() {
            // If GET call is successful
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;

                // Try getting the JSON results
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.d(TAG, "Results: " + results.get(0).toString());
                    JSONObject movie = (JSONObject) results.get(0);
                    videoId = movie.getString("key");
                    playVideoListener(videoId);

                    // If it fails to get JSON results catch exception
                } catch(Exception e) {
                    Log.e(TAG, "Hit JSON exception", e);
                }
            }
            // If GET call fails
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
        return videoId;
    }

}