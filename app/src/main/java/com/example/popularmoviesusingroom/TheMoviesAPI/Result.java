package com.example.popularmoviesusingroom.TheMoviesAPI;

import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("id")
    int id;
    @SerializedName("original_title")
    String original_title;
    @SerializedName("poster_path")
    String poster_path;
    @SerializedName("overview")
    String overview;
    @SerializedName("release_date")
    String release_date;
    @SerializedName("popularity")
    Double popularity;
    @SerializedName("vote_average")
    Double user_rating;

    public int getId() {
        return id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Double getUser_rating() {
        return user_rating;
    }
}
