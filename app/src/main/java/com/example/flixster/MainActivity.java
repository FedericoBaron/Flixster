package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    // List of movie objects
    List<Movie> movies;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView rvMovies = binding.rvMovies;
        movies = new ArrayList<>();

        // Create the adapter
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        // Set the adapter on the Recycler View
        rvMovies.setAdapter(movieAdapter);

        // Set the Layout Manager on the Recycler View
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        // Calls API to get all the movies for Recycler View
        getAPI(movieAdapter);
    }

    //GET API call to Movies
    private void getAPI(final MovieAdapter movieAdapter) {

        // Gets the api key from secret XML and puts it in URL
        String movies_key = getString(R.string.movies_api_key);

        // URL to call Movies GET API to get all now playing movies
        final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + movies_key;

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            // If GET call is successful
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;

                // Try getting the JSON results
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());

                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();

                    Log.i(TAG, "Movies: " + movies.size());

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
    }
}