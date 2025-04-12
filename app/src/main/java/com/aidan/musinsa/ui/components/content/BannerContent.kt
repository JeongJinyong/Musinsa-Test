package com.aidan.musinsa.ui.components.content

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aidan.musinsa.data.model.Banner
import com.aidan.musinsa.ui.theme.MusinsaTestTheme
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/**
 * 배너 아이템 컴포넌트
 *
 * @param banner 배너 모델
 * @param onClick 배너 클릭 이벤트 핸들러
 */
@Composable
fun BannerItem(
    banner: Banner,
    onClick: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clickable { onClick(banner.linkURL) },
    ) {
        // 배너 이미지
        AsyncImage(
            model = banner.thumbnailURL,
            contentDescription = banner.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )

        // 제목, 설명, 키워드 부분
        if (banner.title.isNotEmpty() || banner.description.isNotEmpty() || banner.keyword.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                // 키워드가 있는 경우
                if (banner.keyword.isNotEmpty()) {
                    Text(
                        text = banner.keyword,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.DarkGray.copy(alpha = 0.7f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // 제목이 있는 경우
                if (banner.title.isNotEmpty()) {
                    Text(
                        text = banner.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // 설명이 있는 경우
                if (banner.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = banner.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

/**
 * 배너 페이징 인디케이터 컴포넌트 (숫자 형식)
 *
 * @param totalCount 전체 배너 수
 * @param currentPage 현재 페이지 인덱스 (1부터 시작)
 */
@Composable
fun BannerPagerIndicator(
    totalCount: Int,
    currentPage: Int
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.DarkGray.copy(alpha = 0.7f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$currentPage/$totalCount",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * 배너 콘텐츠 컴포넌트 - 무한 스크롤 구현
 *
 * @param banners 배너 목록
 * @param onBannerClick 배너 클릭 이벤트 핸들러
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerContent(
    banners: List<Banner>,
    onBannerClick: (String) -> Unit = {},
) {
    if (banners.isEmpty()) return

    val pageCount = banners.size
    val infinitePageCount = 10000

    val actualPageCount = if (pageCount > 1) infinitePageCount else 1

    val initialPage = if (pageCount > 1) (infinitePageCount / 2) - ((infinitePageCount / 2) % pageCount) else 0

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { actualPageCount }
    )

    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    val currentRealPage = remember(pagerState.currentPage) {
        (pagerState.currentPage % pageCount) + 1
    }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage < initialPage / 4 || pagerState.currentPage > initialPage * 1.75) {
            val targetPage = initialPage + (pagerState.currentPage % pageCount)
            pagerState.scrollToPage(targetPage)
        }
    }

    if (pageCount > 1) {
        LaunchedEffect(key1 = Unit) {
            try {
                while (true) {
                    delay(3000)
                    if (!isDragged && isActive) {
                        val nextPage = pagerState.currentPage + 1
                        if (nextPage < actualPageCount) {
                            pagerState.animateScrollToPage(
                                page = nextPage,
                                animationSpec = tween(durationMillis = 800)
                            )
                        } else {
                            pagerState.scrollToPage(initialPage + (pagerState.currentPage % pageCount))
                        }
                    }
                }
            } catch (e: CancellationException) {
                // 코루틴 취소 처리 (의도적인 취소를 무시하지 않음)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomEnd
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            beyondBoundsPageCount = 1,
            userScrollEnabled = pageCount > 1
        ) { virtualPage ->
            val realIndex = virtualPage % pageCount
            BannerItem(
                banner = banners[realIndex],
                onClick = onBannerClick
            )
        }

        if (pageCount > 1) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                BannerPagerIndicator(
                    totalCount = pageCount,
                    currentPage = currentRealPage
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BannerContentPreview() {
    MusinsaTestTheme {
        BannerContent(
            banners = listOf(
                Banner(
                    linkURL = "https://www.musinsa.com/app/campaign/index/junebeautyfull",
                    thumbnailURL = "https://image.msscdn.net/images/event_banner/2022061009432800000059650.jpg",
                    title = "",
                    description = "",
                    keyword = ""
                ),
                Banner(
                    linkURL = "https://www.musinsa.com/app/plan/views/22278",
                    thumbnailURL = "https://image.msscdn.net/images/event_banner/2022062311154900000044053.jpg",
                    title = "하이드아웃 S/S 시즌오프",
                    description = "최대 30% 할인",
                    keyword = "세일"
                ),
                Banner(
                    linkURL = "https://www.musinsa.com/app/plan/views/22189",
                    thumbnailURL = "https://image.msscdn.net/images/event_banner/2022062311154700000070083.jpg",
                    title = "오끌레르 22 서머 컬렉션 발매",
                    description = "최대 20% 할인",
                    keyword = "발매"
                ),
                Banner(
                    linkURL = "https://www.musinsa.com/app/plan/views/21902",
                    thumbnailURL = "https://image.msscdn.net/images/event_banner/2022062211345700000040311.jpg",
                    title = "COOL한 여름을 위한 냉감 아이템",
                    description = "최대 54% 할인",
                    keyword = "무신사 추천"
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BannerItemPreview() {
    MusinsaTestTheme {
        BannerItem(
            banner = Banner(
                linkURL = "https://example.com/banner1",
                thumbnailURL = "https://via.placeholder.com/400x225",
                title = "메인 배너 제목",
                description = "배너 설명 텍스트가 여기에 들어갑니다.",
                keyword = "단독세일"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BannerPagerIndicatorPreview() {
    MusinsaTestTheme {
        BannerPagerIndicator(
            totalCount = 5,
            currentPage = 2
        )
    }
}