package com.aidan.musinsa.data.repository

import com.aidan.musinsa.data.model.ApiResponse
import com.aidan.musinsa.data.model.ContentItem
import com.aidan.musinsa.data.remote.ApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class ContentRepositoryTest {

    private val mockApiService = mockk<ApiService>()
    private lateinit var repository: ContentRepository
    
    private val mockContentItems = listOf(
        ContentItem(), ContentItem(), ContentItem()
    )
    
    private val mockApiResponse = ApiResponse(mockContentItems)

    @Before
    fun setup() {
        repository = ContentRepositoryImpl(mockApiService)
    }

    @Test
    fun `getContents 성공 시 API 응답의 data 리스트 반환 확인`() = runTest {
        coEvery { mockApiService.getContents() } returns mockApiResponse
        
        val result = repository.getContents().first()
        
        assertEquals(mockContentItems, result)
    }

    @Test
    fun `getContents 성공 시 데이터 개수 일치 확인`() = runTest {
        coEvery { mockApiService.getContents() } returns mockApiResponse
        
        val result = repository.getContents().first()
        
        assertEquals(mockContentItems.size, result.size)
    }
}