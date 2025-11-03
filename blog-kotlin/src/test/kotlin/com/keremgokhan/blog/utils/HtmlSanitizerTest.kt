package com.keremgokhan.blog.utils

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HtmlSanitizerTest {

    @Test
    fun `sanitize should allow safe HTML tags`() {
        val input = "<p>Hello <strong>World</strong></p>"
        val result = HtmlSanitizer.sanitize(input)

        assertTrue(result.contains("<p>"))
        assertTrue(result.contains("<strong>"))
        assertTrue(result.contains("Hello"))
    }

    @Test
    fun `sanitize should remove script tags`() {
        val input = "<p>Hello</p><script>alert('XSS')</script>"
        val result = HtmlSanitizer.sanitize(input)

        assertTrue(result.contains("<p>"))
        assertFalse(result.contains("<script>"))
        assertFalse(result.contains("alert"))
    }

    @Test
    fun `sanitize should remove onclick attributes`() {
        val input = "<a onclick=\"alert('XSS')\">Click me</a>"
        val result = HtmlSanitizer.sanitize(input)

        assertFalse(result.contains("onclick"))
        assertFalse(result.contains("alert"))
    }

    @Test
    fun `sanitize should allow safe links`() {
        val input = "<a href=\"https://example.com\">Link</a>"
        val result = HtmlSanitizer.sanitize(input)

        assertTrue(result.contains("<a"))
        assertTrue(result.contains("href"))
        assertTrue(result.contains("example.com"))
    }

    @Test
    fun `escapeHtml should escape special characters`() {
        val input = "<script>alert('XSS')</script>"
        val result = HtmlSanitizer.escapeHtml(input)

        assertEquals("&lt;script&gt;alert(&#x27;XSS&#x27;)&lt;/script&gt;", result)
    }

    @Test
    fun `sanitize should handle null input`() {
        val result = HtmlSanitizer.sanitize(null)

        assertEquals("", result)
    }

    @Test
    fun `escapeHtml should handle null input`() {
        val result = HtmlSanitizer.escapeHtml(null)

        assertEquals("", result)
    }
}
