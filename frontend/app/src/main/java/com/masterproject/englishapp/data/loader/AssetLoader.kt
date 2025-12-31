package com.masterproject.englishapp.data.loader

import android.content.Context

interface AssetLoader {
    fun loadText(path: String): String
}

class AndroidAssetLoader(
    private val context: Context
) : AssetLoader {

    override fun loadText(path: String): String {
        return context.assets.open(path).bufferedReader().use {
            it.readText()
        }
    }
}