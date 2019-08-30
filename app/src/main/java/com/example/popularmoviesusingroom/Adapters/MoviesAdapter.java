package com.example.popularmoviesusingroom.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesusingroom.Model.Movie;
import com.example.popularmoviesusingroom.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends
        RecyclerView.Adapter<MoviesAdapter.ViewHolder>{

     class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.posterIV) ImageView imageView;

        private ViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(this);
            itemView.setOnClickListener(onMovieClickListener);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<Movie> Movies;
    private View.OnClickListener onMovieClickListener;
    private Context context;

    // Pass in the tasks array into the constructor
    public MoviesAdapter(List<Movie> Movies ) {
        this.Movies = Movies;
    }

    public void setMovies(List<Movie> Movies){
     this.Movies = Movies;
    }

    public List<Movie> getMovies(){
        return Movies;
    }

    public void setItemClickListener(View.OnClickListener clickListener) {
        onMovieClickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View TaskView = inflater.inflate(R.layout.single_poster, parent, false);

        // Return a new holder instance
        return new ViewHolder(TaskView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie m = Movies.get(position);
        Toast.makeText(context, m.getPosterPath(), Toast.LENGTH_SHORT).show();
        Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w185/"+m.getPosterPath())
                .placeholder(R.drawable.ic_movie).error(R.drawable.ic_broken_image)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return Movies.size();
    }
}
