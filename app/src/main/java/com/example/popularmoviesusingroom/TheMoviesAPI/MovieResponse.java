package com.example.popularmoviesusingroom.TheMoviesAPI;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {

    @SerializedName("results")
    List<Result> results;

    public List<Result> getResults() {
        return results;
    }
}
