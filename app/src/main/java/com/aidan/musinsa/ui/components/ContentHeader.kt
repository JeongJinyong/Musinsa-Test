package com.aidan.musinsa.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.aidan.musinsa.ui.theme.MusinsaTestTheme

/**
 * 콘텐츠 섹션의 헤더 컴포넌트
 *
 * @param title 헤더 제목
 * @param iconUrl 헤더 아이콘 URL (없는 경우 null)
 * @param linkUrl 헤더 링크 URL (없는 경우 null)
 * @param onAllClick 전체 버튼 클릭 이벤트 핸들러
 */
@Composable
fun ContentHeader(
    title: String,
    iconUrl: String? = null,
    linkUrl: String? = null,
    onAllClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 제목 표시
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        // 아이콘이 있는 경우 아이콘 표시
        if (!iconUrl.isNullOrEmpty()) {
            Spacer(modifier = Modifier.width(8.dp))
            AsyncImage(
                model = iconUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Fit
            )
        }

        // 링크 URL이 있는 경우 '전체' 버튼 표시
        if (!linkUrl.isNullOrEmpty()) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "전체",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable { onAllClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentHeaderPreview_TitleOnly() {
    MusinsaTestTheme {
        ContentHeader(
            title = "클리어런스"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContentHeaderPreview_WithIcon() {
    MusinsaTestTheme {
        ContentHeader(
            title = "클리어런스",
            iconUrl = "https://example.com/icon.png"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContentHeaderPreview_WithLink() {
    MusinsaTestTheme {
        ContentHeader(
            title = "클리어런스",
            linkUrl = "https://example.com/all"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContentHeaderPreview_WithIconAndLink() {
    MusinsaTestTheme {
        ContentHeader(
            title = "클리어런스",
            iconUrl = "https://example.com/icon.png",
            linkUrl = "https://example.com/all"
        )
    }
}