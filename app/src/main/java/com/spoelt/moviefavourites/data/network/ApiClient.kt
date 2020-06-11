package com.spoelt.moviefavourites.data.network

import com.spoelt.moviefavourites.data.network.ApiEndpoints.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    private fun okHttpClient(): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.connectTimeout(2, TimeUnit.MINUTES)
        okHttpClient.readTimeout(2, TimeUnit.MINUTES)
        okHttpClient.writeTimeout(2, TimeUnit.MINUTES)

        return okHttpClient
    }

    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        val okhttpBuilder = okHttpClient()
        when (retrofit) {
            null -> retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpBuilder.build())
                .build()
        }
        return retrofit as Retrofit
    }
}