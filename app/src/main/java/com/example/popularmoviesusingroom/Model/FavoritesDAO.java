package com.example.popularmoviesusingroom.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoritesDAO {

    @Query("SELECT * FROM Favorites")
    LiveData<List<Movie>> loadFavorites();

    @Insert
    void insertMovie(Movie m);

    @Delete
    void deleteMovie(Movie m);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie m);

    @Query("SELECT * FROM Favorites WHERE id = :id")
    LiveData<Movie> loadMovieById(int id);
}
