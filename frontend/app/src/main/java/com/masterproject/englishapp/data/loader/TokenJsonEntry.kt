package com.masterproject.englishapp.data.loader

@JvmInline
value class TokenJsonValues(val raw: String) {
    fun split(): List<String> =
        raw.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}

data class TokenJsonEntry(
    val values: TokenJsonValues,
    val example: String
)