package com.masterproject.englishapp.components.optiongrid

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.components.cards.Rect3DCard

@Composable
fun TextImageAnswerOption(
    modifier: Modifier = Modifier,
    text: String,
    image: Painter?,
    aspectRatio: Float = 1f,
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    textColor: Color = Color.Black,
    borderWidth: Dp = 2.dp,
    borderColor: Color = Color.Blue,
    innerPadding: Dp = 16.dp,
    imageTextSpacing: Dp = 8.dp,
    textOnTop: Boolean = false,
    onClick: () -> Unit
) {
    Rect3DCard(
        modifier = modifier
            .aspectRatio(aspectRatio),
        borderWidth = borderWidth,
        borderColor = borderColor,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val textContent: @Composable () -> Unit = {
                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    color = textColor,
                    softWrap = true
                )
            }

            val imageContent: @Composable () -> Unit = {
                image?.let {
                    Image(
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier.weight(1f, fill = false),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            if (textOnTop) {
                textContent()
                Spacer(modifier = Modifier.height(imageTextSpacing))
                imageContent()
            } else {
                imageContent()
                Spacer(modifier = Modifier.height(imageTextSpacing))
                textContent()
            }
        }
    }
}