package com.codepath.rizb13.flixter.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.rizb13.flixter.BR;
import com.codepath.rizb13.flixter.DetailActivity;
import com.codepath.rizb13.flixter.R;
import com.codepath.rizb13.flixter.databinding.ActivityDetailBinding;
import com.codepath.rizb13.flixter.databinding.ItemMovieBinding;
import com.codepath.rizb13.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {


    Context context;
    List<Movie> movies;
    DetailActivity detailActivity;
    AsyncHttpClient client = new AsyncHttpClient();

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);

       //LayoutInflater layoutInflater =
                //LayoutInflater.from(parent.getContext());
        //ViewDataBinding binding = DataBindingUtil.inflate(
                //layoutInflater, viewType, parent, false);
        //itemMovieBinding = ItemMovieBinding.bind(movieView);

        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder " + position);
        //get movie at the position:
        Movie movie = movies.get(position);
        //Bind movie data into VH:
        holder.bind(movie);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout container;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        RatingBar ratingBar;
        //private final ViewDataBinding binding;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //this.binding = binding;

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            container = itemView.findViewById(R.id.container);
        }

        @SuppressLint("DefaultLocale")
        public void bind(Movie movie) {
            // binding.setVariable(BR._all, movie);
            //binding.executePendingBindings();
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            String imageUrl;
            //if Landscape
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //imageUrl = backdropPath
                imageUrl = movie.getBackdropPath();
            }else{
                //imageUrl = posterPath
                imageUrl = movie.getPosterPath();
            }
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new RoundedCorners(5));
            Glide.with(context)
                    .load(imageUrl)
                    .circleCrop()
                    .transform(new RoundedCornersTransformation(30,15))
                    .into(ivPoster);

            //register quick listener on container:

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //navigate to new activity:
                    Intent i = new Intent(context,DetailActivity.class);
                    i.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(i);
                }
        });


}}}
