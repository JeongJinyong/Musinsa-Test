package com.aidan.musinsa.ui.components.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aidan.musinsa.data.model.Good
import com.aidan.musinsa.ui.theme.MusinsaTestTheme

/**
 * 상품 아이템 컴포넌트
 *
 * @param good 상품 모델
 * @param onClick 상품 클릭 이벤트 핸들러
 */
@Composable
fun GoodItem(
    good: Good,
    onClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { onClick(good.linkURL) }
            .padding(4.dp)
    ) {
        // 상품 이미지 (브랜드명과 쿠폰 오버레이)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            // 상품 이미지
            AsyncImage(
                model = good.thumbnailURL,
                contentDescription = good.brandName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(4.dp))
            )

            // 브랜드명 (왼쪽 하단 오버레이)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = good.brandName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // 쿠폰 (오른쪽 상단 오버레이)
            if (good.hasCoupon) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "쿠폰",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 가격 정보 (할인율 → 원래가격 → 현재가격)
        // 할인율이 있는 경우
        if (good.saleRate > 0) {
            // 할인율
            Text(
                text = "${good.saleRate}%",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )

            // 원래 가격 (할인 전)
            val originalPrice = good.price * 100 / (100 - good.saleRate)
            Text(
                text = "${originalPrice}원",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textDecoration = TextDecoration.LineThrough
            )
        }

        // 현재 가격
        Text(
            text = "${good.price}원",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * 그리드 콘텐츠 컴포넌트
 *
 * @param goods 상품 목록
 * @param expandedLines 확장된 행 수
 * @param onGoodClick 상품 클릭 이벤트 핸들러
 */
@Composable
fun GridContent(
    goods: List<Good>,
    expandedLines: Int = 0,
    onGoodClick: (String) -> Unit = {}
) {
    if (goods.isEmpty()) return

    val rowCount = 2 + expandedLines
    val itemsToShow = minOf(goods.size, rowCount * 3)
    val goodsToShow = goods.take(itemsToShow)

    Column(modifier = Modifier.fillMaxWidth()) {
        for (i in 0 until itemsToShow step 3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (j in i until minOf(i + 3, itemsToShow)) {
                    GoodItem(
                        good = goodsToShow[j],
                        onClick = onGoodClick,
                        modifier = Modifier.weight(1f)
                    )
                }
                for (j in 0 until 3 - minOf(3, itemsToShow - i)) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            if (i < itemsToShow - 3) Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GoodItemPreview() {
    MusinsaTestTheme {
        GoodItem(
            good = Good(
                linkURL = "https://www.musinsa.com/app/goods/2281818",
                thumbnailURL = "https://image.msscdn.net/images/goods_img/20211224/2281818/2281818_1_320.jpg",
                brandName = "아스트랄 프로젝션",
                price = 39900,
                saleRate = 50,
                hasCoupon = true
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GridContentPreview() {
    MusinsaTestTheme {
        GridContent(
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
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GridContentExpandedPreview() {
    MusinsaTestTheme {
        GridContent(
            goods = List(12) { index ->
                Good(
                    linkURL = "https://example.com/good$index",
                    thumbnailURL = "https://via.placeholder.com/150",
                    brandName = "브랜드$index",
                    price = 30000 + (index * 5000),
                    saleRate = if (index % 2 == 0) 20 else 0,
                    hasCoupon = index % 3 == 0
                )
            },
            expandedLines = 2
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GridContentTwoPreview() {
    MusinsaTestTheme {
        GridContent(
            goods = List(2) { index ->
                Good(
                    linkURL = "https://example.com/good$index",
                    thumbnailURL = "https://via.placeholder.com/150",
                    brandName = "브랜드$index",
                    price = 30000 + (index * 5000),
                    saleRate = if (index % 2 == 0) 20 else 0,
                    hasCoupon = index % 3 == 0
                )
            },
            expandedLines = 2
        )
    }
}