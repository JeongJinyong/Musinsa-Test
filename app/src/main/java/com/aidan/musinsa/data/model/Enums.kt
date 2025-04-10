package com.aidan.musinsa.data.model

/**
 * 콘텐츠 타입 Enum
 */
enum class ContentType {
    BANNER,
    GRID,
    SCROLL,
    STYLE,
    UNKNOWN;
    
    companion object {
        /**
         * 매칭되는 값이 없으면 UNKNOWN 반환
         */
        fun fromString(value: String): ContentType = 
            entries.find { it.name == value } ?: UNKNOWN
    }
}

/**
 * 푸터 타입 Enum
 */
enum class FooterType {
    REFRESH,
    MORE,
    UNKNOWN;
    
    companion object {
        /**
         * 매칭되는 값이 없으면 UNKNOWN 반환
         */
        fun fromString(value: String): FooterType = 
            entries.find { it.name == value } ?: UNKNOWN
    }
}
