package com.example.popularmoviesusingroom;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesusingroom.Adapters.MoviesAdapter;
import com.example.popularmoviesusingroom.Model.AppDatabase;
import com.example.popularmoviesusingroom.Model.FavoritesViewModel;
import com.example.popularmoviesusingroom.Model.Movie;
import com.example.popularmoviesusingroom.TheMoviesAPI.MovieResponse;
import com.example.popularmoviesusingroom.TheMoviesAPI.MoviesService;
import com.example.popularmoviesusingroom.TheMoviesAPI.Result;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainRecyclerView) RecyclerView RecyclerView;
    private ArrayList<Movie> movies = new ArrayList<>();
    private MoviesAdapter moviesAdapter;

    private LiveData<List<Movie>> favorites;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        db = AppDatabase.getInstance(getApplicationContext());

        setTitle("Movies");
        RecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        moviesAdapter = new MoviesAdapter(movies);
        RecyclerView.setAdapter(moviesAdapter);
        moviesAdapter.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();
                Intent n = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                Gson gson = new Gson();
                n.putExtra(Movie.class.getName(), gson.toJson(movies.get(position)));
                startActivity(n);
            }
        });
        if (savedInstanceState == null) {
            getPopularMovies();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular:
                getPopularMovies();
                break;
            case R.id.top_rated:
                getTopRatedMovies();
                break;
            case R.id.favorite:
                setUpViewModel();
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putParcelableArrayList("MoviesList",movies);
        outState.putInt("Layout" , RecyclerView.getScrollState());
        outState.putParcelable("Layout" , RecyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState, outPersistentState);
        Log.d("MyLog","onSavedInstanseState");
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        movies = savedInstanceState.getParcelableArrayList("MoviesList");
        moviesAdapter.notifyDataSetChanged();
        Log.d("MyLog","onRestoreInstanseState");
    }

    @Override
    protected void onResume() {
        super.onResume();
        moviesAdapter.notifyDataSetChanged();
        Log.d("MyLog","onResume");
    }

    private void getPopularMovies() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesService service = retrofit.create(MoviesService.class);
        //Run the Request
        service.getPopular(Constants.YOUR_API_KEY)
                .enqueue(new Callback<MovieResponse>() {

                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                       convertResponseToMovies(response);
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
    }
    private void convertResponseToMovies(Response<MovieResponse> response){
        movies.clear();
        List<Result> results = response.body().getResults();
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getPoster_path() != null) {
                movies.add(new Movie(results.get(i).getId(), results.get(i).getPoster_path(),
                        results.get(i).getOriginal_title(), results.get(i).getRelease_date(),
                        results.get(i).getOverview(), results.get(i).getPopularity(),
                        results.get(i).getUser_rating()));
            }
        }
        moviesAdapter.notifyDataSetChanged();
    }
    private void getTopRatedMovies() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //status code header
        //200-300 successful
        //400 bad request
        //401/403 unauthorized/forbidden
        //500+ server error

        MoviesService service = retrofit.create(MoviesService.class);
        //Run the Request
        service.getTopRated(Constants.YOUR_API_KEY)
                .enqueue(new Callback<MovieResponse>() {

                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        convertResponseToMovies(response);
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
    }
    public void setUpViewModel() {
        Log.d("MyLog","setUpViewModel");
        movies.clear();
        FavoritesViewModel viewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        //movies.addAll(favorites);
        viewModel.getFavorites().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> fMovies) {
                movies.addAll(fMovies);
                moviesAdapter.notifyDataSetChanged();
            }
        });
    }
}
