package com.masterproject.englishapp.screens.lessons.videos.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.masterproject.englishapp.components.VerticalStepper
import com.masterproject.englishapp.components.YouTubePlayer
import com.masterproject.englishapp.screens.lessons.videos.Lesson

@Composable
fun LessonCard(
    lesson: Lesson,
    index: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {

        VerticalStepper(index = index, circleSize = 22.dp, lineTopOffset = 24.dp)

        Spacer(modifier = Modifier.width(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column {
                YouTubePlayer(
                    youtubeId = lesson.youtubeId,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )

                Text(
                    text = lesson.title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp)
                )

                Text(
                    text = lesson.description,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    }
}