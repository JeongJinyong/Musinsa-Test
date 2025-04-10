package com.aidan.musinsa.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aidan.musinsa.ui.theme.MusinsaTestTheme

/**
 * 콘텐츠 푸터 버튼 컴포넌트
 * 
 * @param title 버튼에 표시할 텍스트
 * @param iconUrl 버튼에 표시할 아이콘 URL (없으면 null)
 * @param onClick 버튼 클릭 이벤트 핸들러
 */
@Composable
fun ContentFooter(
    title: String,
    iconUrl: String? = null,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.DarkGray
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // 아이콘이 있는 경우 아이콘 표시
            if (!iconUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = iconUrl,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            
            // 버튼 제목 표시 (가운데 정렬)
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentFooterPreview_TitleOnly() {
    MusinsaTestTheme {
        ContentFooter(
            title = "새로운 추천"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContentFooterPreview_WithIcon() {
    MusinsaTestTheme {
        ContentFooter(
            title = "새로운 추천",
            iconUrl = "https://example.com/icon.png"
        )
    }
}