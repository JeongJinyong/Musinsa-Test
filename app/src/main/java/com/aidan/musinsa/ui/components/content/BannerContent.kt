package com.aidan.musinsa.ui.components.content

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clickable { onClick(banner.linkURL) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box {
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

    val virtualPageCount = Int.MAX_VALUE
    val initialPage = (virtualPageCount / 2) - ((virtualPageCount / 2) % banners.size)
    
    val pagerState = rememberPagerState(initialPage = initialPage) { virtualPageCount }

    val currentPage by remember {
        derivedStateOf {
            (pagerState.currentPage % banners.size) + 1
        }
    }
    
    // 자동 슬라이드 효과
    if (banners.size > 1) {
        LaunchedEffect(pagerState) {
            while (true) {
                delay(3000)
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    }

    Box {
        // 배너 페이저
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { virtualPage ->
            // 실제 배너 인덱스 계산
            val realIndex = virtualPage % banners.size
            BannerItem(
                banner = banners[realIndex],
                onClick = onBannerClick
            )
        }

        // 인디케이터 (배너가 2개 이상인 경우에만 표시)
        if (banners.size > 1) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                BannerPagerIndicator(
                    totalCount = banners.size,
                    currentPage = currentPage
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
            banners = List(5) { index ->
                Banner(
                    linkURL = "https://example.com/banner$index",
                    thumbnailURL = "https://via.placeholder.com/400x225",
                    title = "배너 제목 ${index + 1}",
                    description = "배너 ${index + 1}의 설명 텍스트입니다. 세부 내용이 여기에 표시됩니다.",
                    keyword = when (index % 3) {
                        0 -> "세일"
                        1 -> "단독세일"
                        else -> "한정세일"
                    }
                )
            }
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