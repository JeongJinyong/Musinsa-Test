package com.aidan.musinsa.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aidan.musinsa.ui.theme.MusinsaTestTheme

/**
 * 에러 상태를 표시하는 컴포넌트
 *
 * @param message 표시할 에러 메시지
 * @param onRetry 재시도 버튼 클릭 이벤트 핸들러 (null인 경우 버튼 표시 안함)
 */
@Composable
fun ErrorView(
    message: String,
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.stat_notify_error),
            contentDescription = "Error",
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        if (onRetry != null) {
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRetry,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "다시 시도")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorViewPreview() {
    MusinsaTestTheme {
        ErrorView(
            message = "콘텐츠를 불러오는 중 오류가 발생했습니다."
        ) {
            // 재시도 동작
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorViewWithoutRetryPreview() {
    MusinsaTestTheme {
        ErrorView(
            message = "네트워크 연결을 확인해주세요."
        )
    }
}