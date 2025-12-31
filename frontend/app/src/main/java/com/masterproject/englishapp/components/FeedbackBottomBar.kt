package com.masterproject.englishapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FeedbackBottomBar(
    title: String,
    message: String,
    isCorrect: Boolean,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isCorrect) Color(0xFFD7FFB8) else Color(0xFFFFDFE0)
    val contentColor = if (isCorrect) Color(0xFF58A700) else Color(0xFFEA2B2B)
    val buttonColor = if (isCorrect) Color(0xFF58CC02) else Color(0xFFFF4B4B)

    Surface(
        color = backgroundColor,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .navigationBarsPadding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = contentColor
                        )
                    )
                    if (message.isNotEmpty()) {
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = contentColor
                            )
                        )
                    }
                }

                // Ícones de comentário/flag como na imagem (opcional)
                //Row {
                //    Icon(Icons.Default.ChatBubbleOutline, null, tint = contentColor, modifier = Modifier.padding(8.dp))
                //    Icon(Icons.Default.OutlinedFlag, null, tint = contentColor, modifier = Modifier.padding(8.dp))
                //}
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = onContinue
            ) {
                Text(
                    "CONTINUE",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                )
            }
        }
    }
}

