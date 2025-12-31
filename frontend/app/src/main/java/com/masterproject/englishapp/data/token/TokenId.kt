package com.masterproject.englishapp.data.token

import com.masterproject.englishapp.data.Category
import com.masterproject.englishapp.grammar.GClass

data class TokenId(
    val grammarClass: GClass,
    val category: Category,
    val localId: String
) {
    override fun toString(): String =
        "${grammarClass.name}/${category.name}/$localId"
}