package com.aidan.musinsa.ui.feature.home

import com.aidan.musinsa.data.model.ContentItem
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized

/**
 * Home 화면의 상태를 저장하는 State 클래스
 * - 콘텐츠 목록 상태와 확장 상태만 관리
 */
data class HomeState(
    // 콘텐츠 목록 상태 (로딩/오류/성공)
    val contentItems: Async<List<ContentItem>> = Uninitialized,
    
    // 콘텐츠별 확장 상태 (인덱스 -> 추가 행 수)
    val expandedContents: Map<Int, Int> = emptyMap(),

    // 각 콘텐츠의 현재 표시된 행 수
    val displayedLines: Map<Int, Int> = emptyMap()
) : MavericksState {
    /**
     * 특정 콘텐츠의 확장 상태 확인
     * @param index 콘텐츠 인덱스
     * @return 추가된 행 수
     */
    fun getExpandedLines(index: Int): Int = expandedContents[index] ?: 0

    /**
     * 특정 콘텐츠의 현재 표시된 행 수 확인
     * @param index 콘텐츠 인덱스
     * @return 표시된 행 수
     */
    fun getDisplayedLines(index: Int): Int = displayedLines[index] ?: 1

    /**
     * 특정 콘텐츠의 더보기 가능 여부 확인
     * @param index 콘텐츠 인덱스
     * @param totalLines 전체 행 수
     * @return 더보기 가능 여부
     */
    fun canLoadMore(index: Int, totalLines: Int): Boolean {
        return getDisplayedLines(index) < totalLines
    }
}