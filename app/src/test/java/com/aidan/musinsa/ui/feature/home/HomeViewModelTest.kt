package com.aidan.musinsa.ui.feature.home

import com.aidan.musinsa.data.model.Banner
import com.aidan.musinsa.data.model.Content
import com.aidan.musinsa.data.model.ContentItem
import com.aidan.musinsa.data.model.Footer
import com.aidan.musinsa.data.model.Good
import com.aidan.musinsa.data.model.Header
import com.aidan.musinsa.data.repository.ContentRepository
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.test.MavericksTestRule
import com.airbnb.mvrx.withState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class HomeViewModelTest {

    @get:Rule
    val mavericksTestRule = MavericksTestRule()

    private val testDispatcher = StandardTestDispatcher()
    private val mockRepository = mockk<ContentRepository>()
    private lateinit var viewModel: HomeViewModel

    private fun createTestGoods(count: Int): List<Good> {
        return (1..count).map { index ->
            Good(
                linkURL = "https://example.com/good$index",
                thumbnailURL = "https://example.com/good$index.jpg",
                brandName = "Brand ${('A'.code + index - 1).toChar()}",
                price = 10000 * index,
                saleRate = (index * 5) % 30,
                hasCoupon = index % 2 == 1
            )
        }
    }


    private fun createTestBanners(count: Int): List<Banner> {
        return (1..count).map { index ->
            Banner(
                linkURL = "https://example.com/banner$index",
                thumbnailURL = "https://example.com/thumbnail$index.jpg",
                title = "Banner $index",
                description = "Description $index",
                keyword = "Keyword$index"
            )
        }
    }

    // 테스트 아이템 데이터
    private val testItems by lazy {
        listOf(
            ContentItem(
                contents = Content(
                    typeString = "BANNER",
                    banners = createTestBanners(3)
                ),
                header = Header("Banner Header"),
                footer = Footer("Banner Footer", typeString = "REFRESH")
            ),
            ContentItem(
                contents = Content(
                    typeString = "GRID",
                    goods = createTestGoods(12)
                ),
                header = Header("Goods Header"),
                footer = Footer("Goods Footer", typeString = "MORE")
            )
        )
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { mockRepository.getContents() } returns flowOf(testItems)
        viewModel = HomeViewModel(HomeState(), mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `콘텐츠 로드 성공 시 State 업데이트 확인`() = runTest {
        advanceUntilIdle()
        // 초기 뷰모델 생성 시 loadContents()가 호출되어 상태 업데이트
        val state = withState(viewModel) { it }
        assertTrue(state.contentItems is Success)
        assertEquals(testItems, state.contentItems())
    }

    @Test
    fun `REFRESH 푸터 클릭 시 콘텐츠 아이템 유지 확인`() = runTest {
        advanceUntilIdle()
        // 첫 번째 콘텐츠의 푸터(REFRESH) 클릭
        viewModel.refreshContents(0)
        advanceUntilIdle()
        // 콘텐츠가 변경되었지만 아이템 수는 유지
        val state = withState(viewModel) { it }
        assertTrue(state.contentItems is Success)
        assertEquals(testItems.size, state.contentItems()?.size)
    }

    @Test
    fun `MORE 푸터 클릭 시 확장 상태 업데이트 확인`() = runTest {
        advanceUntilIdle()

        // 초기 상태 확인
        var state = withState(viewModel) { it }
        assertEquals(0, state.expandedContents[1] ?: 0)

        // 확장 요청
        viewModel.expandContent(1)

        advanceUntilIdle()

        // 확장 상태 업데이트 확인
        state = withState(viewModel) { it }
        assertEquals(1, state.expandedContents[1] ?: 0)

        // 추가 확장 요청
        viewModel.expandContent(1)

        advanceUntilIdle()

        // 확장 상태 추가 업데이트 확인
        state = withState(viewModel) { it }
        assertEquals(2, state.expandedContents[1] ?: 0)
    }

    @Test
    fun `푸터 클릭 처리 시 타입에 따른 적절한 동작 확인`() = runTest {
        advanceUntilIdle()

        // MORE 타입 푸터 클릭
        viewModel.onFooterClick(1)

        advanceUntilIdle()

        // 확장 상태 업데이트 확인
        val state = withState(viewModel) { it }
        assertEquals(1, state.expandedContents[1] ?: 0)
    }
}