package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    // The movie to display
    Movie movie;

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

}