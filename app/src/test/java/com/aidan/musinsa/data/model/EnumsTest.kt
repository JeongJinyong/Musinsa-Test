package com.aidan.musinsa.data.model

import org.junit.Test
import kotlin.test.assertEquals

class EnumsTest {

    @Test
    fun `ContentType fromString 메소드가 올바른 타입을 반환하는지 확인`() {
        assertEquals(ContentType.BANNER, ContentType.fromString("BANNER"))
        assertEquals(ContentType.GRID, ContentType.fromString("GRID"))
        assertEquals(ContentType.SCROLL, ContentType.fromString("SCROLL"))
        assertEquals(ContentType.STYLE, ContentType.fromString("STYLE"))
    }

    @Test
    fun `ContentType fromString 메소드가 알 수 없는 타입에 대해 UNKNOWN을 반환하는지 확인`() {
        assertEquals(ContentType.UNKNOWN, ContentType.fromString("INVALID_TYPE"))
        assertEquals(ContentType.UNKNOWN, ContentType.fromString(""))
        assertEquals(ContentType.UNKNOWN, ContentType.fromString("banner")) // 대소문자 구분
    }

    @Test
    fun `FooterType fromString 메소드가 올바른 타입을 반환하는지 확인`() {
        assertEquals(FooterType.REFRESH, FooterType.fromString("REFRESH"))
        assertEquals(FooterType.MORE, FooterType.fromString("MORE"))
    }

    @Test
    fun `FooterType fromString 메소드가 알 수 없는 타입에 대해 UNKNOWN을 반환하는지 확인`() {
        assertEquals(FooterType.UNKNOWN, FooterType.fromString("INVALID_TYPE"))
        assertEquals(FooterType.UNKNOWN, FooterType.fromString(""))
        assertEquals(FooterType.UNKNOWN, FooterType.fromString("refresh")) // 대소문자 구분
    }
    
    @Test
    fun `Content 모델의 type getter가 올바른 ContentType을 반환하는지 확인`() {
        val bannerContent = Content(typeString = "BANNER")
        val gridContent = Content(typeString = "GRID")
        val scrollContent = Content(typeString = "SCROLL")
        val styleContent = Content(typeString = "STYLE")
        val unknownContent = Content(typeString = "INVALID_TYPE")
        
        assertEquals(ContentType.BANNER, bannerContent.type)
        assertEquals(ContentType.GRID, gridContent.type)
        assertEquals(ContentType.SCROLL, scrollContent.type)
        assertEquals(ContentType.STYLE, styleContent.type)
        assertEquals(ContentType.UNKNOWN, unknownContent.type)
    }
    
    @Test
    fun `Footer 모델의 type getter가 올바른 FooterType을 반환하는지 확인`() {
        val refreshFooter = Footer(title = "새로고침", typeString = "REFRESH")
        val moreFooter = Footer(title = "더보기", typeString = "MORE")
        val unknownFooter = Footer(title = "알 수 없음", typeString = "INVALID_TYPE")
        
        assertEquals(FooterType.REFRESH, refreshFooter.type)
        assertEquals(FooterType.MORE, moreFooter.type)
        assertEquals(FooterType.UNKNOWN, unknownFooter.type)
    }
}