package com.example.weatherapp.remote

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.io.IOException
import java.util.concurrent.TimeUnit


class Networking {

    companion object {
        val instance: Networking by lazy { Networking() }
    }
    private val retrofit: WeatherAPI

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.apply { loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(loggingInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(EndPoints.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create()
    }

    fun getRetrofit(): WeatherAPI {
        return retrofit
    }

    class ApiKeyInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val original: Request = chain.request()
            val url: HttpUrl = original.url.newBuilder()
                .addQueryParameter("appid", EndPoints.API_KEY)
                .build()
            val request: Request = original.newBuilder()
                .url(url)
                .build()
            return chain.proceed(request)
        }
    }
}