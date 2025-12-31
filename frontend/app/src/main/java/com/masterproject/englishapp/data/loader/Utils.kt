package com.masterproject.englishapp.data.loader

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.masterproject.englishapp.data.token.AnyToken
import com.masterproject.englishapp.data.token.TokenId
import com.masterproject.englishapp.grammar.GClass

fun GClass.folderName(): String =
    when (this) {
        GClass.NOUN -> "nouns"
        GClass.VERB -> "verbs"
        GClass.ADJECTIVE -> "adjectives"
    }

inline fun <reified T> parseJson(json: String): T {
    val type = object : TypeToken<T>() {}.type
    return Gson().fromJson(json, type)
}

@SuppressLint("DiscouragedApi")
fun getImageOfToken(
    context: Context,
    tokenId: TokenId
): Int? {

    val resourceName =
        "${tokenId.category.name.lowercase()}_${tokenId.localId.lowercase()}"

    val resId = context.resources.getIdentifier(
        resourceName,
        "drawable",
        context.packageName
    )

    return resId.takeIf { it != 0 }
}
