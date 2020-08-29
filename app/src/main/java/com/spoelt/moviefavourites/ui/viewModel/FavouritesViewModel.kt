package com.spoelt.moviefavourites.ui.viewModel

import androidx.lifecycle.ViewModel
import com.spoelt.moviefavourites.data.database.MovieDao

class FavouritesViewModel(val database: MovieDao) : ViewModel() {
    val favouriteMovies = database.getAllMovies()
}