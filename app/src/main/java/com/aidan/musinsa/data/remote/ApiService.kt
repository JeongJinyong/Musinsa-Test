package com.aidan.musinsa.data.remote

import com.aidan.musinsa.data.model.ApiResponse
import retrofit2.http.GET

/**
 * Retrofit API 인터페이스
 */
interface ApiService {
    @GET("interview/list.json")
    suspend fun getContents(): ApiResponse

    companion object {
        const val BASE_URL = "https://meta.musinsa.com/"
    }
}