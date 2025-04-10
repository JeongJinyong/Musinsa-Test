package com.aidan.musinsa.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API 응답 모델
 */
@Serializable
data class ApiResponse(
    @SerialName("data") val data: List<ContentItem> = emptyList()
)

/**
 * 콘텐츠 아이템 모델 (data 배열의 각 항목)
 */
@Serializable
data class ContentItem(
    @SerialName("contents") val contents: Content? = null,
    @SerialName("header") val header: Header? = null,
    @SerialName("footer") val footer: Footer? = null
)

/**
 * 콘텐츠 모델
 */
@Serializable
data class Content(
    @SerialName("type") val typeString: String,
    @SerialName("banners") val banners: List<Banner> = emptyList(),
    @SerialName("goods") val goods: List<Good> = emptyList(),
    @SerialName("styles") val styles: List<Style> = emptyList()
) {
    val type: ContentType get() = ContentType.fromString(typeString)
}

/**
 * 배너 모델
 */
@Serializable
data class Banner(
    @SerialName("linkURL") val linkURL: String,
    @SerialName("thumbnailURL") val thumbnailURL: String,
    @SerialName("title") val title: String = "",
    @SerialName("description") val description: String = "",
    @SerialName("keyword") val keyword: String = ""
)

/**
 * 상품 모델
 */
@Serializable
data class Good(
    @SerialName("linkURL") val linkURL: String,
    @SerialName("thumbnailURL") val thumbnailURL: String,
    @SerialName("brandName") val brandName: String,
    @SerialName("price") val price: Int,
    @SerialName("saleRate") val saleRate: Int,
    @SerialName("hasCoupon") val hasCoupon: Boolean
)

/**
 * 스타일 모델
 */
@Serializable
data class Style(
    @SerialName("linkURL") val linkURL: String,
    @SerialName("thumbnailURL") val thumbnailURL: String
)

/**
 * 헤더 모델
 */
@Serializable
data class Header(
    @SerialName("title") val title: String,
    @SerialName("iconURL") val iconURL: String? = null,
    @SerialName("linkURL") val linkURL: String? = null
)

/**
 * 푸터 모델
 */
@Serializable
data class Footer(
    @SerialName("title") val title: String,
    @SerialName("iconURL") val iconURL: String? = null,
    @SerialName("type") val typeString: String
) {
    val type: FooterType get() = FooterType.fromString(typeString)
}

