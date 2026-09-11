package com.noniboy.struja.di

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.noniboy.struja.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
        // Body logging only in debug builds, with the base64 image payload redacted
        // (it is multi-MB and would leak the photo + API key into logcat).
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor { message ->
                Log.d(
                    "OkHttp",
                    message.replace(
                        Regex("\"data\"\\s*:\\s*\"[^\"]{64,}\""),
                        "\"data\":\"<base64-redacted>\""
                    )
                )
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}
