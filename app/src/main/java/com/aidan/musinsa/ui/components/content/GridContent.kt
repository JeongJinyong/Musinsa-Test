package com.aidan.musinsa.ui.components.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
    onClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(good.linkURL) }
            .padding(8.dp)
    ) {
        // 상품 이미지 (브랜드명과 쿠폰 오버레이)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            // 상품 이미지
            Card(
                modifier = Modifier.fillMaxSize(),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                AsyncImage(
                    model = good.thumbnailURL,
                    contentDescription = good.brandName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // 브랜드명 (왼쪽 하단 오버레이)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
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
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "쿠폰",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

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

    // 기본 2행(6개) + 확장된 행 수만큼 표시
    val rowCount = 2 + expandedLines
    val itemsToShow = minOf(goods.size, rowCount * 3) // 3열 그리드
    val goodsToShow = goods.take(itemsToShow)

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
        userScrollEnabled = false // 스크롤 비활성화
    ) {
        items(goodsToShow) { good ->
            GoodItem(
                good = good,
                onClick = onGoodClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GoodItemPreview() {
    MusinsaTestTheme {
        GoodItem(
            good = Good(
                linkURL = "https://example.com/good1",
                thumbnailURL = "https://via.placeholder.com/150",
                brandName = "예시브랜드",
                price = 59000,
                saleRate = 20,
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
                    linkURL = "https://example.com/good1",
                    thumbnailURL = "https://via.placeholder.com/150",
                    brandName = "브랜드A",
                    price = 59000,
                    saleRate = 20,
                    hasCoupon = true
                ),
                Good(
                    linkURL = "https://example.com/good2",
                    thumbnailURL = "https://via.placeholder.com/150",
                    brandName = "브랜드B",
                    price = 32000,
                    saleRate = 0,
                    hasCoupon = false
                ),
                Good(
                    linkURL = "https://example.com/good3",
                    thumbnailURL = "https://via.placeholder.com/150",
                    brandName = "브랜드C",
                    price = 42000,
                    saleRate = 10,
                    hasCoupon = false
                ),
                Good(
                    linkURL = "https://example.com/good4",
                    thumbnailURL = "https://via.placeholder.com/150",
                    brandName = "브랜드D",
                    price = 76000,
                    saleRate = 30,
                    hasCoupon = true
                ),
                Good(
                    linkURL = "https://example.com/good5",
                    thumbnailURL = "https://via.placeholder.com/150",
                    brandName = "브랜드E",
                    price = 25000,
                    saleRate = 0,
                    hasCoupon = false
                ),
                Good(
                    linkURL = "https://example.com/good6",
                    thumbnailURL = "https://via.placeholder.com/150",
                    brandName = "브랜드F",
                    price = 89000,
                    saleRate = 15,
                    hasCoupon = true
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