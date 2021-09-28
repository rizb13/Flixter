package com.codepath.rizb13.flixter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.rizb13.flixter.databinding.ActivityDetailBinding;
import com.codepath.rizb13.flixter.databinding.ActivityMainBinding;
import com.codepath.rizb13.flixter.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {


    private static final String YOUTUBE_API_KEY = "AIzaSyADJD7O09mhalzvQgOmG7qAmGkIp6xs_GA";
    public static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";


    private ActivityDetailBinding binding;

    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingBar;

    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail);

        tvTitle = binding.tvTitle;   //findViewById(R.id.tvTitle);
        tvOverview = binding.tvOverview; //findViewById(R.id.tvOverview);
        ratingBar = binding.ratingBar; // findViewById(R.id.ratingBar);
        youTubePlayerView = binding.player; //findViewById(R.id.player);

        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating((float)movie.getRating());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS_URL,movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {

                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if (results.length() == 0){
                        return;
                    }
                    String youtubeKey = results.getJSONObject(0).getString("key");
                    double rating = movie.getRating();
                    Log.d("DetailActivity",youtubeKey);
                    initializeYouTube(youtubeKey,rating);

                } catch (JSONException e) {
                    Log.e("DetailActivity","Hit json exception");
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });
    }

    public void initializeYouTube(final String youtubeKey, final double ratting) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {


                Log.d("DetailActivity", "onInitializationSuccess");

               if(ratting > 5.0)
                    youTubePlayer.loadVideo(youtubeKey);
               else
                   youTubePlayer.cueVideo(youtubeKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                Log.d("DetailActivity", "onInitializationfailure");
            }
        });
    }
}