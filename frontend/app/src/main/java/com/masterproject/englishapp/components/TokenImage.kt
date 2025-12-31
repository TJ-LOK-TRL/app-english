package com.masterproject.englishapp.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import com.masterproject.englishapp.data.loader.getImageOfToken
import com.masterproject.englishapp.data.token.AnyToken
import com.masterproject.englishapp.data.token.TokenId

@Composable
fun tokenImagePainter(tokenId: TokenId): Painter? {
    val context = LocalContext.current

    val resId = getImageOfToken(context, tokenId) ?: return null

    return painterResource(id = resId)
}

@Composable
fun tokenImagePainter(token: AnyToken): Painter? = tokenImagePainter(token.id)