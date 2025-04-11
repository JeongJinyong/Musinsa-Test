package com.aidan.musinsa.ui.components.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        items(goods) { good ->
            GoodItem(
                good = good,
                onClick = onGoodClick,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScrollContentPreview() {
    MusinsaTestTheme {
        ScrollContent(
            goods = List(10) { index ->
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