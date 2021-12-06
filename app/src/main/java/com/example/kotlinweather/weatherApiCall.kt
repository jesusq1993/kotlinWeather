package com.example.kotlinweather

import android.telecom.Call
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val API_KEY = "f8a4dbbd33f337ab3a73c76dedbf1f58"


interface weatherApiCall {

    //https://api.openweathermap.org/data/2.5/weather?q=Tyler,Texas&appid=f8a4dbbd33f337ab3a73c76dedbf1f58

    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") cityName: String
    ): Response<WeatherModel>

    companion object {
        operator fun invoke():weatherApiCall {
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("appid", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(weatherApiCall::class.java)
        }
    }
}