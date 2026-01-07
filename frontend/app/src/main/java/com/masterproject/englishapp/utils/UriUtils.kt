package com.masterproject.englishapp.utils

import java.net.URLEncoder

object UrlBuilder {
    fun buildUrl(
        basePath: String,
        params: Map<String, Any> = emptyMap(),
        fragment: String? = null
    ): String {
        val queryString = if (params.isNotEmpty()) {
            "?" + params.toQueryString()
        } else ""

        val fragmentPart = fragment?.let { "#$it" } ?: ""

        return "$basePath$queryString$fragmentPart"
    }

    private fun Map<String, Any>.toQueryString(): String {
        return entries.joinToString("&") { (key, value) ->
            "${key.urlEncode()}=${value.toString().urlEncode()}"
        }
    }

    private fun String.urlEncode(): String {
        return URLEncoder.encode(this, "UTF-8")
    }
}