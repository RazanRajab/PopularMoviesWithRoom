package com.example.popularmoviesusingroom.TheMoviesAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesService {

    //https://api.themoviedb.org/3
    @GET("movie/popular")
    Call<MovieResponse> getPopular(@Query("api_key") String token);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRated(@Query("api_key") String token);

    @GET("movie/{movie_id}/videos")
    Call<TrailersResponse> getTrailers(@Path("movie_id") int id, @Query("api_key") String token);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewsResponse> getReviews(@Path("movie_id") int id, @Query("api_key") String token);
}
