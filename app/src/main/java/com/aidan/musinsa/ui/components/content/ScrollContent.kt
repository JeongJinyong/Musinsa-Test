package com.aidan.musinsa.ui.components.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aidan.musinsa.data.model.Good
import com.aidan.musinsa.ui.theme.MusinsaTestTheme

/**
 * 횡스크롤 콘텐츠 컴포넌트
 *
 * @param goods 상품 목록
 * @param onGoodClick 상품 클릭 이벤트 핸들러
 */
@Composable
fun ScrollContent(
    goods: List<Good>,
    onGoodClick: (String) -> Unit = {}
) {
    if (goods.isEmpty()) return


    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val itemWidth = when {
        screenWidth > 600.dp -> 180.dp
        else -> screenWidth * 0.38f
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        items(goods) { good ->
            GoodItem(
                good = good,
                onClick = onGoodClick,
                modifier = Modifier
                    .width(itemWidth)
                    .wrapContentHeight()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScrollContentPreview() {
    MusinsaTestTheme {
        ScrollContent(
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
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScrollContentFewItemsPreview() {
    MusinsaTestTheme {
        ScrollContent(
            goods = List(3) { index ->
                Good(
                    linkURL = "https://example.com/good$index",
                    thumbnailURL = "https://via.placeholder.com/150",
                    brandName = "브랜드$index",
                    price = 30000 + (index * 5000),
                    saleRate = if (index % 2 == 0) 20 else 0,
                    hasCoupon = index % 3 == 0
                )
            }
        )
    }
}