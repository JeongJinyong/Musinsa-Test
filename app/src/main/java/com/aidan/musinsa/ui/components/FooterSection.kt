package com.aidan.musinsa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aidan.musinsa.data.model.Footer
import com.aidan.musinsa.data.model.FooterType
import com.aidan.musinsa.ui.theme.MusinsaTestTheme

/**
 * Footer 모델을 사용하는 푸터 섹션 컴포넌트
 *
 * @param footer Footer 모델 객체
 * @param onFooterClick 푸터 클릭 이벤트 핸들러
 * @param canExpand 더보기 가능 여부
 */
@Composable
fun FooterSection(
    footer: Footer,
    onFooterClick: () -> Unit = {},
    canExpand: Boolean = true
) {
    if (footer.type != FooterType.UNKNOWN && 
        (footer.type == FooterType.REFRESH || (footer.type == FooterType.MORE && canExpand))) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color.LightGray.copy(alpha = 0.2f))
        ) {
            ContentFooter(
                title = when (footer.type) {
                    FooterType.REFRESH -> footer.title.ifEmpty { "새로운 추천" }
                    FooterType.MORE -> footer.title.ifEmpty { "더보기" }
                    FooterType.UNKNOWN -> footer.title
                },
                iconUrl = footer.iconURL,
                onClick = onFooterClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FooterSectionPreview_Refresh() {
    MusinsaTestTheme {
        FooterSection(
            footer = Footer(
                title = "새로운 추천",
                typeString = "REFRESH"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FooterSectionPreview_More() {
    MusinsaTestTheme {
        FooterSection(
            footer = Footer(
                title = "더보기",
                typeString = "MORE"
            ),
            canExpand = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FooterSectionPreview_More_Disabled() {
    MusinsaTestTheme {
        FooterSection(
            footer = Footer(
                title = "더보기",
                typeString = "MORE"
            ),
            canExpand = false
        )
    }
}