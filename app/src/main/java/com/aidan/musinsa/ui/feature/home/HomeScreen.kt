package com.aidan.musinsa.ui.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aidan.musinsa.data.model.Banner
import com.aidan.musinsa.data.model.Content
import com.aidan.musinsa.data.model.ContentItem
import com.aidan.musinsa.data.model.ContentType
import com.aidan.musinsa.data.model.Footer
import com.aidan.musinsa.data.model.Good
import com.aidan.musinsa.data.model.Header
import com.aidan.musinsa.data.model.Style
import com.aidan.musinsa.ui.components.ErrorView
import com.aidan.musinsa.ui.components.FooterSection
import com.aidan.musinsa.ui.components.HeaderSection
import com.aidan.musinsa.ui.components.LoadingView
import com.aidan.musinsa.ui.components.content.BannerContent
import com.aidan.musinsa.ui.components.content.GridContent
import com.aidan.musinsa.ui.components.content.ScrollContent
import com.aidan.musinsa.ui.components.content.StyleContent
import com.aidan.musinsa.ui.theme.MusinsaTestTheme
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = mavericksViewModel()
    
    val contentItemsState = viewModel.collectAsState { it.contentItems }
    val expandedContents = viewModel.collectAsState { it.expandedContents }
    val displayedLines = viewModel.collectAsState { it.displayedLines }
    
    val uriHandler = LocalUriHandler.current

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = contentItemsState.value) {
                is Loading -> {
                    LoadingView(message = "콘텐츠를 불러오는 중입니다...")
                }
                is Fail -> {
                    ErrorView(
                        message = "콘텐츠를 불러오는 중 오류가 발생했습니다.\n${state.error.localizedMessage}",
                        onRetry = { viewModel.loadContents() }
                    )
                }
                is Success -> {
                    val contentItems = state.invoke()
                    if (contentItems.isNullOrEmpty()) {
                        ErrorView(message = "표시할 콘텐츠가 없습니다.")
                    } else {
                        HomeContent(
                            contentItems = contentItems,
                            expandedContents = expandedContents.value,
                            displayedLines = displayedLines.value,
                            onLinkClick = { url -> uriHandler.openUri(url) },
                            onFooterClick = { index -> viewModel.onFooterClick(index) }
                        )
                    }
                }
                else -> {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

@Composable
fun HomeContent(
    contentItems: List<ContentItem>,
    expandedContents: Map<Int, Int>,
    displayedLines: Map<Int, Int>,
    onLinkClick: (String) -> Unit,
    onFooterClick: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        itemsIndexed(contentItems) { index, item ->
            ContentItemSection(
                contentItem = item,
                expandedLines = expandedContents[index] ?: 0,
                displayedLines = displayedLines[index] ?: 1,
                onLinkClick = onLinkClick,
                onFooterClick = { onFooterClick(index) }
            )
            
            if (index < contentItems.size - 1) {
                Divider(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    thickness = 8.dp
                )
            }
        }
    }
}

@Composable
fun ContentItemSection(
    contentItem: ContentItem,
    expandedLines: Int,
    displayedLines: Int,
    onLinkClick: (String) -> Unit,
    onFooterClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            contentItem.header?.let { header ->
                HeaderSection(
                    header = header,
                    onAllClick = { url -> onLinkClick(url) }
                )
            }

            contentItem.contents?.let { content ->
                when (content.type) {
                    ContentType.BANNER -> {
                        BannerContent(
                            banners = content.banners,
                            onBannerClick = onLinkClick
                        )
                    }
                    ContentType.GRID -> {
                        GridContent(
                            goods = content.goods,
                            expandedLines = expandedLines,
                            onGoodClick = onLinkClick
                        )
                    }
                    ContentType.SCROLL -> {
                        ScrollContent(
                            goods = content.goods,
                            onGoodClick = onLinkClick
                        )
                    }
                    ContentType.STYLE -> {
                        StyleContent(
                            styles = content.styles,
                            expandedLines = expandedLines,
                            onStyleClick = onLinkClick
                        )
                    }
                    ContentType.UNKNOWN -> {
                        // 알 수 없는 타입은 아무것도 표시하지 않음
                    }
                }
            }
            
            contentItem.footer?.let { footer ->
                // 더 이상 표시할 데이터가 없으면 Footer를 숨김
                val totalLines = when(contentItem.contents?.type) {
                    ContentType.GRID -> {
                        val totalGoods = contentItem.contents.goods.size
                        (totalGoods + 2) / 3
                    }
                    ContentType.STYLE -> {
                        val totalStyles = contentItem.contents.styles.size
                        (totalStyles + 2) / 3
                    }
                    else -> 1
                }

                if (displayedLines < totalLines) {
                    FooterSection(
                        footer = footer,
                        onFooterClick = onFooterClick
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    MusinsaTestTheme {
        val mockContentItems = listOf(
            // 배너 섹션
            ContentItem(
                contents = Content(
                    typeString = "BANNER",
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
                        )
                    )
                )
            ),
            // 클리어런스 그리드 섹션
            ContentItem(
                header = Header(title = "클리어런스"),
                contents = Content(
                    typeString = "GRID",
                    goods = listOf(
                        Good(
                            linkURL = "https://www.musinsa.com/app/goods/2281818",
                            thumbnailURL = "https://image.msscdn.net/images/goods_img/20211224/2281818/2281818_1_320.jpg",
                            brandName = "아스트랄 프로젝션",
                            price = 39900,
                            saleRate = 50,
                            hasCoupon = true
                        ),
                        Good(
                            linkURL = "https://www.musinsa.com/app/goods/2281817",
                            thumbnailURL = "https://image.msscdn.net/images/goods_img/20211224/2281817/2281817_1_320.jpg",
                            brandName = "아스트랄 프로젝션",
                            price = 39900,
                            saleRate = 45,
                            hasCoupon = false
                        ),
                        Good(
                            linkURL = "https://www.musinsa.com/app/goods/2281819",
                            thumbnailURL = "https://image.msscdn.net/images/goods_img/20211224/2281819/2281819_1_320.jpg",
                            brandName = "아스트랄 프로젝션",
                            price = 39900,
                            saleRate = 65,
                            hasCoupon = true
                        ),
                        Good(
                            linkURL = "https://www.musinsa.com/app/goods/2281822",
                            thumbnailURL = "https://image.msscdn.net/images/goods_img/20211224/2281822/2281822_1_320.jpg",
                            brandName = "아스트랄 프로젝션",
                            price = 39900,
                            saleRate = 75,
                            hasCoupon = false
                        ),
                        Good(
                            linkURL = "https://www.musinsa.com/app/goods/2281823",
                            thumbnailURL = "https://image.msscdn.net/images/goods_img/20211224/2281823/2281823_1_320.jpg",
                            brandName = "아스트랄 프로젝션",
                            price = 39900,
                            saleRate = 35,
                            hasCoupon = true
                        ),
                        Good(
                            linkURL = "https://www.musinsa.com/app/goods/2281826",
                            thumbnailURL = "https://image.msscdn.net/images/goods_img/20211224/2281826/2281826_1_320.jpg",
                            brandName = "아스트랄 프로젝션",
                            price = 39900,
                            saleRate = 73,
                            hasCoupon = false
                        )
                    )
                ),
                footer = Footer(
                    title = "더보기",
                    typeString = "MORE"
                )
            ),
            // 디스커버리 익스페디션 스크롤 섹션
            ContentItem(
                header = Header(
                    title = "디스커버리 익스페디션 인기 스니커즈: 최대 50% 할인",
                    iconURL = "https://image.msscdn.net/icons/mobile/clock.png",
                    linkURL = "https://www.musinsa.com/brands/discoveryexpedition?category3DepthCodes=&category2DepthCodes=&category1DepthCode=018&colorCodes=&startPrice=&endPrice=&exclusiveYn=&includeSoldOut=&saleGoods=&timeSale=&includeKeywords=&sortCode=discount_rate&tags=&page=1&size=90&listViewType=small&campaignCode=&groupSale=&outletGoods=false&boutiqueGoods="
                ),
                contents = Content(
                    typeString = "SCROLL",
                    goods = listOf(
                        Good(
                            linkURL = "https://www.musinsa.com/app/goods/1727824",
                            thumbnailURL = "https://image.msscdn.net/images/goods_img/20201221/1727824/1727824_4_320.jpg",
                            brandName = "디스커버리 익스페디션",
                            price = 59500,
                            saleRate = 50,
                            hasCoupon = true
                        ),
                        Good(
                            linkURL = "https://www.musinsa.com/app/goods/2309841",
                            thumbnailURL = "https://image.msscdn.net/images/goods_img/20220117/2309841/2309841_2_320.jpg",
                            brandName = "디스커버리 익스페디션",
                            price = 68000,
                            saleRate = 20,
                            hasCoupon = false
                        ),
                        Good(
                            linkURL = "https://www.musinsa.com/app/goods/2175693",
                            thumbnailURL = "https://image.msscdn.net/images/goods_img/20211013/2175693/2175693_2_320.jpg",
                            brandName = "디스커버리 익스페디션",
                            price = 132300,
                            saleRate = 30,
                            hasCoupon = true
                        ),
                        Good(
                            linkURL = "https://www.musinsa.com/app/goods/1795481",
                            thumbnailURL = "https://image.msscdn.net/images/goods_img/20210216/1795481/1795481_2_320.jpg",
                            brandName = "디스커버리 익스페디션",
                            price = 87200,
                            saleRate = 20,
                            hasCoupon = false
                        )
                    )
                ),
                footer = Footer(
                    title = "새로운 추천",
                    iconURL = "https://image.msscdn.net/icons/mobile/clock.png",
                    typeString = "REFRESH"
                )
            ),
            // 무신사 추천 코디 스타일 섹션
            ContentItem(
                header = Header(title = "무신사 추천 코디"),
                contents = Content(
                    typeString = "STYLE",
                    styles = listOf(
                        Style(
                            linkURL = "https://www.musinsa.com/app/styles/views/27417",
                            thumbnailURL = "https://image.musinsa.com/images/style/list/2022062214302100000008217.jpg"
                        ),
                        Style(
                            linkURL = "https://www.musinsa.com/app/styles/views/27416",
                            thumbnailURL = "https://image.musinsa.com/images/style/list/2022062214285200000072520.jpg"
                        ),
                        Style(
                            linkURL = "https://www.musinsa.com/app/styles/views/27415",
                            thumbnailURL = "https://image.musinsa.com/images/style/list/2022062214272200000056964.jpg"
                        ),
                        Style(
                            linkURL = "https://www.musinsa.com/app/styles/views/27414",
                            thumbnailURL = "https://image.musinsa.com/images/style/list/2022062214255500000030807.jpg"
                        ),
                        Style(
                            linkURL = "https://www.musinsa.com/app/styles/views/27413",
                            thumbnailURL = "https://image.musinsa.com/images/style/list/2022062214232800000082313.jpg"
                        ),
                        Style(
                            linkURL = "https://www.musinsa.com/app/styles/views/27412",
                            thumbnailURL = "https://image.musinsa.com/images/style/list/2022062214214600000026102.jpg"
                        )
                    )
                ),
                footer = Footer(
                    title = "더보기",
                    typeString = "MORE"
                )
            )
        )
        
        HomeContent(
            contentItems = mockContentItems,
            expandedContents = mapOf(1 to 1, 3 to 1),
            displayedLines = mapOf(1 to 2, 3 to 2),
            onLinkClick = {},
            onFooterClick = {}
        )
    }
}