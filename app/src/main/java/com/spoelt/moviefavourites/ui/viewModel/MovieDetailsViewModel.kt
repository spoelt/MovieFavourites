package com.spoelt.moviefavourites.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val _currentMovie = MutableLiveData<Movie>()
    val currentMovie: LiveData<Movie>
        get() = _currentMovie

    init {
        checkMovieInDatabase()
    }

    private fun checkMovieInDatabase() {
        uiScope.launch {
            _currentMovie.value = getMovieFromDatabase(_currentMovie.value?.id)
        }
    }

    private suspend fun getMovieFromDatabase(id: Int?): Movie? {
        return withContext(Dispatchers.IO) {
            val movie = id?.let { database.getMovie(it) }
            movie
        }
    }

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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}