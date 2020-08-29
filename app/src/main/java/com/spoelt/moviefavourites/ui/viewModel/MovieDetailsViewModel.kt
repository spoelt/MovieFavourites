package com.spoelt.moviefavourites.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.spoelt.moviefavourites.data.database.MovieDao
import com.spoelt.moviefavourites.data.model.Movie
import kotlinx.coroutines.*

// AndroidViewModel() is the same as ViewModel, but it takes the application context as a parameter
// and makes it available as a property.
class MovieDetailsViewModel(val database: MovieDao, application: Application) :
    AndroidViewModel(application) {
    // viewModelJob allows you to cancel all coroutines started by this view model when the view
    // model is no longer used and is destroyed
    private var viewModelJob = Job()
    // Using Dispatchers.Main means that coroutines launched in the uiScope will run on the main
    // thread. This is sensible for many coroutines started by a ViewModel, because after these
    // coroutines perform some processing, they result in an update of the UI.
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val favouriteMovies = database.getAllMovies()

    fun saveMovieAsFavourite(movie: Movie): Boolean {
        uiScope.launch {
            insertMovieIntoDb(movie)
        }

        return true
    }

    private suspend fun insertMovieIntoDb(movie: Movie) {
        withContext(Dispatchers.IO) {
            database.insert(movie)
        }
    }

    fun deleteMovieFromFavourites(movie: Movie): Boolean {
        uiScope.launch {
            deleteMovie(movie)
        }

        return true
    }

    private suspend fun deleteMovie(movie: Movie) {
        withContext(Dispatchers.IO) {
            database.delete(movie)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}