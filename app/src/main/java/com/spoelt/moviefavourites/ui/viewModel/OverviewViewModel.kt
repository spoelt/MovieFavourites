package com.spoelt.moviefavourites.ui.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spoelt.moviefavourites.constants.MAX_NUM_PAGES
import com.spoelt.moviefavourites.data.model.JsonResponse
import com.spoelt.moviefavourites.data.model.Movie
import com.spoelt.moviefavourites.data.network.ApiClient
import com.spoelt.moviefavourites.data.network.ApiService
import com.spoelt.moviefavourites.data.states.NetworkState
import kotlinx.coroutines.launch
import java.io.IOException

class OverviewViewModel : ViewModel() {
    private val apiService = ApiClient().getClient().create(ApiService::class.java)
    private var page = 1
    var errorMessage: MutableLiveData<String> = MutableLiveData()
    var movieList: MutableLiveData<MutableList<Movie>> = MutableLiveData()

    init {
        getMovies()
    }

    fun getMovies() {
        viewModelScope.launch {
            val moviesResult = fetchPopularMovies()
            handleResult(moviesResult)
        }

    }

    private suspend fun fetchPopularMovies(): NetworkState {
        return try {
            val response = apiService.getPopularMovies(page = page)
            if (response.isSuccessful) {
                NetworkState.Success(response.body()!!)
            } else {
                when (response.code()) {
                    403 -> NetworkState.HttpErrors.ResourceForbidden(response.message())
                    404 -> NetworkState.HttpErrors.ResourceNotFound(response.message())
                    500 -> NetworkState.HttpErrors.InternalServerError(response.message())
                    502 -> NetworkState.HttpErrors.BadGateWay(response.message())
                    301 -> NetworkState.HttpErrors.ResourceRemoved(response.message())
                    302 -> NetworkState.HttpErrors.RemovedResourceFound(response.message())
                    else -> NetworkState.Error(response.message())
                }
            }
        } catch (error: IOException) {
            NetworkState.NetworkException(error.message!!)
        }
    }

    private fun handleResult(networkState: NetworkState) {
        return when (networkState) {
            is NetworkState.Success -> fillMovieList(networkState.data)
            is NetworkState.HttpErrors.ResourceForbidden -> handleError(networkState.exception)
            is NetworkState.HttpErrors.ResourceNotFound -> handleError(networkState.exception)
            is NetworkState.HttpErrors.InternalServerError -> handleError(networkState.exception)
            is NetworkState.HttpErrors.BadGateWay -> handleError(networkState.exception)
            is NetworkState.HttpErrors.ResourceRemoved -> handleError(networkState.exception)
            is NetworkState.HttpErrors.RemovedResourceFound -> handleError(networkState.exception)
            is NetworkState.Error -> handleError(networkState.error)
            is NetworkState.NetworkException -> handleError(networkState.error)
        }
    }

    private fun handleError(message: String) {
        errorMessage.value = message
    }

    private fun fillMovieList(data: JsonResponse) {
        val list = ArrayList<Movie>()

        for (d in data.results) {
            Log.d("TAG", d.title)
            list.add(d)
        }

        if (page == 1) {
            movieList.value = list
        } else {
            movieList.value?.addAll(list)
        }

        errorMessage.value = ""

        when (page) {
            MAX_NUM_PAGES -> page = 1
            else -> page += 1
        }
    }

    fun refreshMovies() {
        page = 1
        getMovies()
    }

    fun loadNewMovies() {
        getMovies()
    }
}