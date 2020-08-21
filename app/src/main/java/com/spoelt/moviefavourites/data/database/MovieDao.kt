package com.spoelt.moviefavourites.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.spoelt.moviefavourites.data.model.Movie

@Dao
interface MovieDao {
    @Insert
    fun insert(movie: Movie)

    @Delete
    fun delete(movie: Movie)

    @Query("SELECT * from favourite_movies_table WHERE id = :id")
    fun getMovie(id: Int): Movie?

    @Query("SELECT * from favourite_movies_table")
    fun getAllMovies(): LiveData<List<Movie>>
}