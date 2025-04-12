package com.aidan.musinsa.ui.components.content

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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aidan.musinsa.data.model.Style
import com.aidan.musinsa.ui.theme.MusinsaTestTheme

/**
 * 스타일 아이템 컴포넌트
 *
 * @param style 스타일 모델
 * @param onClick 스타일 클릭 이벤트 핸들러
 * @param modifier 컴포넌트에 적용할 수 있는 Modifier
 */
@Composable
fun StyleItem(
    style: Style,
    onClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick(style.linkURL) }
    ) {
        AsyncImage(
            model = style.thumbnailURL,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun StyleContent(
    styles: List<Style>,
    expandedLines: Int = 0,
    onStyleClick: (String) -> Unit = {}
) {
    if (styles.isEmpty()) return

    val spacing = 4.dp
    val imageRatio = 0.8f
    
    // 표시할 스타일 결정
    val topStyles = if (styles.size < 3) styles else styles.take(3)
    val bottomStyles = if (styles.size <= 3) emptyList() else styles.slice(3 until minOf(6, styles.size))
    val extraStyles = if (styles.size > 6 && expandedLines > 0) styles.drop(6).take(expandedLines * 3) else emptyList()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(spacing)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 상단부: 큰 이미지 + 2개 작은 이미지 수직 배치
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                val bigStyle = if (topStyles.isNotEmpty()) topStyles[0] else null
                if (bigStyle != null) {
                    StyleItem(
                        style = bigStyle,
                        onClick = onStyleClick,
                        modifier = Modifier
                            .weight(2f)
                            .aspectRatio(imageRatio)
                    )
                }

                val subStyles = if (topStyles.size > 1) topStyles.subList(1, topStyles.size) else emptyList()
                if (subStyles.isNotEmpty()) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(spacing)
                    ) {
                        subStyles.forEach { style ->
                            StyleItem(
                                style = style,
                                onClick = onStyleClick,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(imageRatio)
                            )
                        }
                    }
                }
            }

            // 하단 스타일 행들 (3개씩 표시)
            val allRowStyles = bottomStyles + extraStyles
            
            for (i in allRowStyles.indices step 3) {
                Spacer(modifier = Modifier.height(spacing))
                
                val rowItems = allRowStyles.slice(i until minOf(i + 3, allRowStyles.size))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    rowItems.forEach { style ->
                        StyleItem(
                            style = style,
                            onClick = onStyleClick,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(imageRatio)
                        )
                    }
                    
                    // 부족한 공간 채우기
                    repeat(3 - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun StyleContentPreview() {
    MusinsaTestTheme {
        StyleContent(
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
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StyleContentExpandedPreview() {
    MusinsaTestTheme {
        StyleContent(
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
                ),
                Style(
                    linkURL = "https://www.musinsa.com/app/styles/views/27411",
                    thumbnailURL = "https://image.musinsa.com/images/style/list/2022062214184600000046790.jpg"
                ),
                Style(
                    linkURL = "https://www.musinsa.com/app/styles/views/27410",
                    thumbnailURL = "https://image.musinsa.com/images/style/list/2022062214165600000031022.jpg"
                ),
                Style(
                    linkURL = "https://www.musinsa.com/app/styles/views/27409",
                    thumbnailURL = "https://image.musinsa.com/images/style/list/2022062214143800000054754.jpg"
                )
            ),
            expandedLines = 2 // 2행 추가
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StyleContentTwoItemsPreview() {
    MusinsaTestTheme {
        StyleContent(
            styles = listOf(
                Style(
                    linkURL = "https://www.musinsa.com/app/styles/views/27417",
                    thumbnailURL = "https://image.musinsa.com/images/style/list/2022062214302100000008217.jpg"
                ),
                Style(
                    linkURL = "https://www.musinsa.com/app/styles/views/27416",
                    thumbnailURL = "https://image.musinsa.com/images/style/list/2022062214285200000072520.jpg"
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StyleContentOneItemPreview() {
    MusinsaTestTheme {
        StyleContent(
            styles = listOf(
                Style(
                    linkURL = "https://www.musinsa.com/app/styles/views/27417",
                    thumbnailURL = "https://image.musinsa.com/images/style/list/2022062214302100000008217.jpg"
                )
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun StyleContentAutoExpandPreview() {
    MusinsaTestTheme {
        StyleContent(
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
                ),
                Style(
                    linkURL = "https://www.musinsa.com/app/styles/views/27411",
                    thumbnailURL = "https://image.musinsa.com/images/style/list/2022062214184600000046790.jpg"
                ),
                Style(
                    linkURL = "https://www.musinsa.com/app/styles/views/27410",
                    thumbnailURL = "https://image.musinsa.com/images/style/list/2022062214165600000031022.jpg"
                ),
                Style(
                    linkURL = "https://www.musinsa.com/app/styles/views/27409",
                    thumbnailURL = "https://image.musinsa.com/images/style/list/2022062214143800000054754.jpg"
                )
            ),
            // expandedLines를 0으로 설정해도 4개 이상일 때 자동으로 1행이 표시됨
            expandedLines = 0
        )
    }
}