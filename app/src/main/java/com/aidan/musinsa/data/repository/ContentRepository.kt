package com.aidan.musinsa.data.repository

import com.aidan.musinsa.data.model.ContentItem
import com.aidan.musinsa.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 콘텐츠 데이터 관리 Repository 인터페이스
 */
interface ContentRepository {
    suspend fun getContents(): Flow<List<ContentItem>>
}

/**
 * ContentRepository 구현체
 */
class ContentRepositoryImpl(
    private val apiService: ApiService
) : ContentRepository {
    
    override suspend fun getContents(): Flow<List<ContentItem>> = flow {
        val response = apiService.getContents()
        emit(response.data)
    }
}