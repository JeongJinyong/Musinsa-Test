package com.aidan.musinsa.ui.components.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    AsyncImage(
        model = style.thumbnailURL,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.background(Color.Blue).clickable { onClick(style.linkURL) }
    )
}

@Composable
fun StyleContent(
    styles: List<Style>,
    expandedLines: Int = 0,
    onStyleClick: (String) -> Unit = {}
) {
    if (styles.isEmpty()) return

    val spacing = 4.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(spacing)
    ) {
        // 상단 0~2번 아이템
        if (styles.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                StyleItem(
                    style = styles[0],
                    onClick = onStyleClick,
                    modifier = Modifier
                        .weight(2f)
                        .aspectRatio(0.5f)
                )

                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    if (styles.size > 1) {
                        StyleItem(
                            style = styles[1],
                            onClick = onStyleClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.5f)
                        )
                    }
                    if (styles.size > 2) {
                        StyleItem(
                            style = styles[2],
                            onClick = onStyleClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.5f)
                        )
                    }
                }
            }
        }

        // 아래 Grid: 3번부터 나머지 (확장된 행 수만큼만 보여줌)
        if (styles.size > 3 && expandedLines > 0) {
            Spacer(modifier = Modifier.height(spacing))

            val rest = styles.drop(3).take(expandedLines * 3)

            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(spacing),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                items(rest) { style ->
                    StyleItem(
                        style = style,
                        onClick = onStyleClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.5f)
                    )
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
            styles = List(10) { index ->
                Style(
                    linkURL = "https://example.com/style$index",
                    thumbnailURL = "https://via.placeholder.com/300x300"
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StyleContentExpandedPreview() {
    MusinsaTestTheme {
        StyleContent(
            styles = List(10) { index ->
                Style(
                    linkURL = "https://example.com/style$index",
                    thumbnailURL = "https://via.placeholder.com/300x300"
                )
            },
            expandedLines = 2 // 2행 추가
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StyleContentTwoItemsPreview() {
    MusinsaTestTheme {
        StyleContent(
            styles = List(3) { index ->
                Style(
                    linkURL = "https://example.com/style$index",
                    thumbnailURL = "https://via.placeholder.com/300x300"
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StyleContentOneItemPreview() {
    MusinsaTestTheme {
        StyleContent(
            styles = List(1) { index ->
                Style(
                    linkURL = "https://example.com/style$index",
                    thumbnailURL = "https://via.placeholder.com/300x300"
                )
            }
        )
    }
}