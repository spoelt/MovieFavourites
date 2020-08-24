package com.spoelt.moviefavourites.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.spoelt.moviefavourites.data.database.MovieDao

class FavouritesViewModel(val database: MovieDao, application: Application) :
    AndroidViewModel(application) {
    val favouriteMovies = database.getAllMovies()
}