package com.spoelt.moviefavourites.data.network

import com.spoelt.moviefavourites.BuildConfig
import com.spoelt.moviefavourites.data.model.JsonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") key: String = BuildConfig.API_KEY,
        @Query("page") page: Int
    ): Response<JsonResponse>
}