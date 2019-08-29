package com.example.popularmoviesusingroom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesusingroom.Adapters.ReviewsAdapter;
import com.example.popularmoviesusingroom.Adapters.TrailersAdapter;
import com.example.popularmoviesusingroom.Model.AppDatabase;
import com.example.popularmoviesusingroom.Model.AppExecutors;
import com.example.popularmoviesusingroom.Model.Movie;
import com.example.popularmoviesusingroom.TheMoviesAPI.MoviesService;
import com.example.popularmoviesusingroom.TheMoviesAPI.Review;
import com.example.popularmoviesusingroom.TheMoviesAPI.ReviewsResponse;
import com.example.popularmoviesusingroom.TheMoviesAPI.Trailer;
import com.example.popularmoviesusingroom.TheMoviesAPI.TrailersResponse;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.reviewsRecycler)
    androidx.recyclerview.widget.RecyclerView RecyclerView;
    @BindView(R.id.trailersRecycler)
    androidx.recyclerview.widget.RecyclerView trailersRecyclerView;
    @BindView(R.id.poster)
    ImageView poster;
    @BindView(R.id.rating)
    TextView rating;
    @BindView(R.id.popularity)
    TextView popularity;
    @BindView(R.id.release_date)
    TextView date;
    @BindView(R.id.overview)
    TextView overview;
    @BindView(R.id.add_favorite)
    Button Favorite;

    private ArrayList<Review> reviews = new ArrayList<>();
    private ReviewsAdapter reviewsAdapter;
    private ArrayList<Trailer> trailers = new ArrayList<>();
    private TrailersAdapter trailersAdapter;
    private Movie m;

    private AppDatabase db;
    private LiveData<Movie> f;
    private boolean fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        String Extra = getIntent().getStringExtra(Movie.class.getName());
        Gson gson = new Gson();
        if (savedInstanceState == null) {
            m = gson.fromJson(Extra, Movie.class);
            getReviews();
            getTrailers();
        }
        setTitle(m.getOriginalTitle());
        db = AppDatabase.getInstance(getApplicationContext());

        RecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        reviewsAdapter = new ReviewsAdapter(reviews);
        RecyclerView.setAdapter(reviewsAdapter);
        reviewsAdapter.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();
                openReview(reviews.get(position));
            }
        });
        trailersRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        trailersAdapter = new TrailersAdapter(trailers);
        trailersRecyclerView.setAdapter(trailersAdapter);
        trailersAdapter.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();
                openVideo(trailers.get(position));
            }
        });

        rating.setText("rating: " + m.getUserRating() + "/10");
        popularity.setText("popularity: " + m.getPopularity() + "");
        date.setText("release date: " + m.getReleaseDate());
        if (!m.getOverview().trim().equals("")) {
            overview.setText(m.getOverview());
        } else {
            overview.setText("No available data");
        }
        Picasso.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w342/" + m.getPosterPath())
                .error(R.drawable.ic_broken_image).placeholder(R.drawable.ic_movie)
                .into(poster);
        Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (fav) {
                            //remove from database
                            //UnFavoriteMovie(m);
                            db.favoritesDAO().deleteMovie(m);
                        } else {
                            //add to database
                            //FavoriteMovie(m);
                            db.favoritesDAO().insertMovie(m);
                        }
                    }
                });
                if (fav) {
                    Favorite.setText("Add to Favorite");
                    Toast.makeText(MovieDetailsActivity.this,
                            "Removed from Favorites", Toast.LENGTH_SHORT).show();
                } else {
                    Favorite.setText("Remove from Favorite");
                    Toast.makeText(MovieDetailsActivity.this,
                            "Added To Favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putParcelableArrayList("reviews", reviews);
        outState.putParcelableArrayList("trailers", trailers);
        outState.putParcelable("movie", m);
        Log.d("MyLog","saveDetailsInstance");
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        reviews = savedInstanceState.getParcelableArrayList("reviews");
        trailers = savedInstanceState.getParcelableArrayList("trailers");
        m = savedInstanceState.getParcelable("movie");
        reviewsAdapter.notifyDataSetChanged();
        trailersAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        f = db.favoritesDAO().loadMovieById(m.getId());
        f.observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                if (f.getValue() != null) {
                    fav = true;
                    Favorite.setText("Remove from Favorite");
                }
                else {
                    fav = false;
                }
            }
        });
        if (fav) {
            Favorite.setText("Remove from Favorite");
        }
        super.onResume();
    }

    private void openReview(Review r) {
        Intent n = new Intent(Intent.ACTION_VIEW, Uri.parse(r.getUrl()));
        startActivity(n);
    }

    private void openVideo(Trailer t) {
        Intent n = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" +
                t.getKey()));
        startActivity(n);
    }

    private void getReviews() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesService service = retrofit.create(MoviesService.class);
        service.getReviews(m.getId(), Constants.YOUR_API_KEY).enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                List<Review> r = response.body().getReviews();

                for (int i = 0; i < r.size(); i++) {
                    reviews.add(new Review(r.get(i).getContent(), r.get(i).getAuthor(), r.get(i).getUrl()));
                }
                reviewsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {

            }
        });
    }

    private void getTrailers() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesService service = retrofit.create(MoviesService.class);
        service.getTrailers(m.getId(), Constants.YOUR_API_KEY).enqueue(new Callback<TrailersResponse>() {
            @Override
            public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                List<Trailer> t = response.body().getTrailers();

                for (int i = 0; i < t.size(); i++) {
                    trailers.add(new Trailer(t.get(i).getKey(), t.get(i).getName()));
                }
                trailersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TrailersResponse> call, Throwable t) {

            }
        });
    }
}
