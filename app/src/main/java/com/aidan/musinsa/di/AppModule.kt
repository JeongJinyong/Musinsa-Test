package com.aidan.musinsa.di

import com.aidan.musinsa.data.remote.ApiService
import com.aidan.musinsa.data.repository.ContentRepository
import com.aidan.musinsa.data.repository.ContentRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Koin DI 모듈
 */
val networkModule = module {
    // JSON 설정
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }
    
    // OkHttpClient
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    // Retrofit
    single {
        val contentType = "application/json".toMediaType()
        Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .client(get())
            .addConverterFactory(get<Json>().asConverterFactory(contentType))
            .build()
    }
    
    // API Service
    single { get<Retrofit>().create(ApiService::class.java) }
    
    // Repository
    single<ContentRepository> { ContentRepositoryImpl(get()) }
}