package com.aidan.musinsa.ui.feature.home

import com.airbnb.mvrx.Uninitialized
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HomeStateTest {

    @Test
    fun `기본 생성자를 사용한 초기 상태 확인`() {
        val initialState = HomeState()
        
        assertTrue(initialState.contentItems is Uninitialized)
        assertEquals(emptyMap<Int, Int>(), initialState.expandedContents)
    }
    
    @Test
    fun `getExpandedLines가 존재하지 않는 인덱스에 대해 0을 반환하는지 확인`() {
        val state = HomeState()
        
        val result = state.getExpandedLines(0)
        
        assertEquals(0, result)
    }
    
    @Test
    fun `getExpandedLines가 존재하는 인덱스에 대해 올바른 값을 반환하는지 확인`() {
        val expandedContents = mapOf(0 to 1, 1 to 2, 2 to 3)
        val state = HomeState(expandedContents = expandedContents)
        
        assertEquals(1, state.getExpandedLines(0))
        assertEquals(2, state.getExpandedLines(1))
        assertEquals(3, state.getExpandedLines(2))
    }
    
    @Test
    fun `copy 메소드가 상태를 올바르게 복제하는지 확인`() {
        val originalState = HomeState(expandedContents = mapOf(0 to 1, 1 to 2))
        
        val copiedState = originalState.copy(expandedContents = originalState.expandedContents + (2 to 3))
        
        assertEquals(3, copiedState.expandedContents.size)
        assertEquals(1, copiedState.expandedContents[0])
        assertEquals(2, copiedState.expandedContents[1])
        assertEquals(3, copiedState.expandedContents[2])
    }
}