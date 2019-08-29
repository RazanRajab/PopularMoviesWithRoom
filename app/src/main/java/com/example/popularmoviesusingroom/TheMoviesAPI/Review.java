package com.example.popularmoviesusingroom.TheMoviesAPI;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Review implements Parcelable {

    @SerializedName("content")
    String content;

    @SerializedName("author")
    String author;

    @SerializedName("url")
    String url;

    public Review(String content, String author, String url) {
        this.content = content;
        this.author = author;
        this.url = url;
    }

    protected Review(Parcel in) {
        content = in.readString();
        author = in.readString();
        url = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getContent() {
        return content;
    }

    public String getAuthor(){
        return author;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(content);
        parcel.writeString(author);
        parcel.writeString(url);
    }
}
