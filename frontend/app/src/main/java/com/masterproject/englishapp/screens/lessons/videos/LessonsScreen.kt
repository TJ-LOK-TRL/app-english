package com.masterproject.englishapp.screens.lessons.videos

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterproject.englishapp.components.VerticalStepper
import com.masterproject.englishapp.components.YouTubePlayer

@Preview(showBackground = true)
@Composable
fun LessonScreen() {

    val lessons = listOf(
        Lesson("Basic Greetings", "How to say hello and goodbye", "YrJy_aymi6M"),         // Greetings & expressions for beginners (YouTube) :contentReference[oaicite:0]{index=0}
        Lesson("Introducing Yourself", "Talk about your name and origin", "rNh3VYiNZL8"),    // English for Beginners #1: Introducing Yourself :contentReference[oaicite:1]{index=1}
        Lesson("Daily Expressions", "Common phrases used every day", "r6nNDYqxEV4"),        // Greetings & Introductions / Self Introduction (YouTube) :contentReference[oaicite:2]{index=2}
        Lesson("Most Used Verbs", "Essential English verbs", "QUjEaAZitRE"),                // 100 English communication sentences (verbs) :contentReference[oaicite:3]{index=3}
        Lesson("Talking About Your Day", "Describe routines", "XQG5A_H5i7Q"),               // How to introduce yourself in English fast! (covers intro + practice) :contentReference[oaicite:4]{index=4}
        Lesson("Asking Questions", "Simple questions in English", "yev4C9q88FQ"),           // Basic Easy English sentences (includes questions) :contentReference[oaicite:5]{index=5}
        Lesson("Numbers and Time", "Dates and time expressions", "zqaDS3Rctys"),             // Learn core English verbs & basic concepts (includes numbers/time) :contentReference[oaicite:6]{index=6}
        Lesson("Ordering Food", "English in restaurants", "MVX4KAZiW7A"),                   // Daily English Conversations: Ordering Food & Drinks :contentReference[oaicite:7]{index=7}
        Lesson("Talking About Places", "Describing locations", "ki2m6Sr3UAU"),               // Ordering at a Restaurant dialogue (useful for places) :contentReference[oaicite:8]{index=8}
        Lesson("Describing People", "Looks and personality", "44FB40olLoM"),                 // English Conversations at the Restaurant for beginners (dialogues include descriptions) :contentReference[oaicite:9]{index=9}
        Lesson("Past and Future", "Basic verb tenses", "zqaDS3Rctys"),                      // Core basic English verbs (context for tenses) :contentReference[oaicite:10]{index=10}
        Lesson("Simple Conversations", "Putting it all together", "MVX4KAZiW7A")            // Daily English ordering food (practice dialogues) :contentReference[oaicite:11]{index=11}
    )

    LazyColumn {
        itemsIndexed(lessons) { index, lesson ->
            LessonCard(
                lesson = lesson,
                index = index + 1
            )
        }
    }
}

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

data class Lesson(
    val title: String,
    val description: String,
    val youtubeId: String
)