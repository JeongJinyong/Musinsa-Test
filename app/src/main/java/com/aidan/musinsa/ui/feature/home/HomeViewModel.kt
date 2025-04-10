package com.aidan.musinsa.ui.feature.home

import com.aidan.musinsa.data.model.ContentType
import com.aidan.musinsa.data.model.FooterType
import com.aidan.musinsa.data.repository.ContentRepository
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.ViewModelContext
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin

/**
 * Home 화면 ViewModel
 * - 콘텐츠 로드, 새로고침, 확장 기능 구현
 * - MVI 아키텍처 패턴 및 Mavericks 라이브러리 활용
 */
class HomeViewModel(
    initialState: HomeState,
    private val contentRepository: ContentRepository
) : MavericksViewModel<HomeState>(initialState) {

    init {
        // 초기화 시 콘텐츠 로드
        loadContents()
    }

    /**
     * 콘텐츠 목록 불러오기
     * - API를 통해 콘텐츠 목록을 가져와 상태 업데이트
     */
    fun loadContents() {
        setState { copy(contentItems = Loading()) }
        
        viewModelScope.launch {
            contentRepository.getContents()
                .execute { contentItemsFlow ->
                    copy(contentItems = contentItemsFlow)
                }
        }
    }

    /**
     * 콘텐츠 새로고침 (REFRESH)
     * - 특정 콘텐츠의 아이템들을 랜덤하게 섞음
     * 
     * @param contentIndex 새로고침할 콘텐츠의 인덱스
     */
    fun refreshContents(contentIndex: Int) {
        withState { state ->
            val contentItems = state.contentItems.invoke() ?: return@withState
            if (contentIndex >= contentItems.size) return@withState
            
            val contentItem = contentItems[contentIndex]
            val content = contentItem.contents ?: return@withState
            
            // 콘텐츠 타입에 따라 다른 아이템 리스트 섞기
            val shuffledContent = when (content.type) {
                ContentType.BANNER -> {
                    if (content.banners.isNotEmpty()) {
                        content.copy(banners = content.banners.shuffled())
                    } else content
                }
                ContentType.GRID, ContentType.SCROLL -> {
                    if (content.goods.isNotEmpty()) {
                        content.copy(goods = content.goods.shuffled())
                    } else content
                }
                ContentType.STYLE -> {
                    if (content.styles.isNotEmpty()) {
                        content.copy(styles = content.styles.shuffled())
                    } else content
                }
                ContentType.UNKNOWN -> content // 알 수 없는 타입은 그대로 반환
            }
            
            // 새로운 콘텐츠 아이템 리스트 생성
            val updatedContentItems = contentItems.toMutableList().apply {
                set(contentIndex, contentItem.copy(contents = shuffledContent))
            }
            
            setState { 
                copy(contentItems = Success(updatedContentItems))
            }
        }
    }

    /**
     * 콘텐츠 확장하기 (MORE)
     * - 특정 콘텐츠에 추가 행 표시
     *
     * @param contentIndex 확장할 콘텐츠의 인덱스
     */
    fun expandContent(contentIndex: Int) {
        withState { state ->
            val currentExpandedLines = state.getExpandedLines(contentIndex)
            val newExpandedLines = currentExpandedLines + 1
            
            setState { 
                copy(expandedContents = expandedContents + (contentIndex to newExpandedLines))
            }
        }
    }
    
    /**
     * 푸터 클릭 이벤트 처리
     * - 푸터 타입에 따라 적절한 액션 수행
     *
     * @param contentIndex 콘텐츠 인덱스
     */
    fun onFooterClick(contentIndex: Int) {
        withState { state ->
            val contentItems = state.contentItems.invoke() ?: return@withState
            if (contentIndex >= contentItems.size) return@withState
            
            val footer = contentItems[contentIndex].footer ?: return@withState
            
            when(footer.type) {
                FooterType.REFRESH -> refreshContents(contentIndex)
                FooterType.MORE -> expandContent(contentIndex)
                FooterType.UNKNOWN -> {} // 알 수 없는 타입은 무시
            }
        }
    }

    /**
     * Mavericks ViewModel 팩토리
     * - 의존성 주입을 통해 ViewModel 생성
     */
    companion object : MavericksViewModelFactory<HomeViewModel, HomeState> {
        override fun create(viewModelContext: ViewModelContext, state: HomeState): HomeViewModel {
            // Koin을 통해 ContentRepository 가져오기
            val repository = getKoin().get<ContentRepository>()
            return HomeViewModel(state, repository)
        }
    }
}