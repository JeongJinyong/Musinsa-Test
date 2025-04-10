package com.aidan.musinsa.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.aidan.musinsa.data.model.Header
import com.aidan.musinsa.ui.theme.MusinsaTestTheme

/**
 * Header 모델을 사용하는 헤더 섹션 컴포넌트
 *
 * @param header Header 모델 객체
 * @param onAllClick 전체 버튼 클릭 이벤트 핸들러
 */
@Composable
fun HeaderSection(
    header: Header,
    onAllClick: (String) -> Unit = {}
) {
    
    ContentHeader(
        title = header.title,
        iconUrl = header.iconURL,
        linkUrl = header.linkURL,
        onAllClick = {
            // linkURL이 있는 경우에만 콜백 호출
            header.linkURL?.let { onAllClick(it) }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HeaderSectionPreview_TitleOnly() {
    MusinsaTestTheme {
        HeaderSection(
            header = Header(
                title = "클리어런스"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderSectionPreview_WithIcon() {
    MusinsaTestTheme {
        HeaderSection(
            header = Header(
                title = "클리어런스",
                iconURL = "https://example.com/icon.png"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderSectionPreview_WithLink() {
    MusinsaTestTheme {
        HeaderSection(
            header = Header(
                title = "클리어런스",
                linkURL = "https://example.com/all"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderSectionPreview_WithIconAndLink() {
    MusinsaTestTheme {
        HeaderSection(
            header = Header(
                title = "클리어런스",
                iconURL = "https://example.com/icon.png",
                linkURL = "https://example.com/all"
            )
        )
    }
}