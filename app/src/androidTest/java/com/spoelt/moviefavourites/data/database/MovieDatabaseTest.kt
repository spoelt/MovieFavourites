package com.spoelt.moviefavourites.data.database

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.spoelt.moviefavourites.data.model.Movie
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class MovieDatabaseTest {
    private lateinit var movieDao: MovieDao
    private lateinit var db: MovieDatabase
    private val PROJECT_POWER = "Project Power"

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        movieDao = db.movieDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertMovie_movieReturned() {
        // Arrange
        val movie = createMovie()
        // Act
        movieDao.insert(movie)
        val specificMovie = movieDao.getMovie(movie.id)
        // Assert
        assertEquals(specificMovie?.title, PROJECT_POWER)
    }

    @Test
    fun deleteMovie_nullReturned() {
        // Arrange
        val movie = createMovie()
        movieDao.insert(movie)
        // Act
        movieDao.delete(movie)
        val retrievedMovie = movieDao.getMovie(movie.id)
        // Assert
        assertEquals(retrievedMovie, null)
    }

    @Test
    fun insertMovies_listOfMoviesReturned() {
        // Arrange
        val movies = createMovieList()
        movies.forEach {
            movieDao.insert(it)
        }
        // Act
        val existingMovies = movieDao.getAllMovies()
        // Assert
        assert(existingMovies.value == movies.sortedWith(compareBy({ it.id }, { it.id })))
    }

    private fun createMovie(): Movie {
        return Movie(
            popularity = 269.671,
            vote_count = 523,
            video = false,
            poster_path = "/bOKjzWDxiDkgEQznhzP4kdeAHNI.jpg",
            id = (0..999999).random(),
            adult = false,
            backdrop_path = "/qVygtf2vU15L2yKS4Ke44U4oMdD.jpg",
            original_language = "en",
            original_title = "Project Power",
            title = "Project Power",
            vote_average = 6.8,
            overview = "Test OverView",
            release_date = "2020-08-14"
        )
    }

    private fun createMovieList(): List<Movie> {
        val movieList = ArrayList<Movie>()

        for (x in 0..5) {
            movieList.add(createMovie())
        }
        return movieList
    }
}