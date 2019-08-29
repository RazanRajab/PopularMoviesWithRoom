package com.example.popularmoviesusingroom.TheMoviesAPI;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersResponse {

    @SerializedName("results")
    List<Trailer> trailers;

    public List<Trailer> getTrailers() {
        return trailers;
    }
}
