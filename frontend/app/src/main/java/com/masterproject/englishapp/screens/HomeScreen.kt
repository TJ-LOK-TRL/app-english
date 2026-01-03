package com.masterproject.englishapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.R
import com.masterproject.englishapp.navigation.Screen
import com.masterproject.englishapp.ui.theme.AppColors
import androidx.compose.foundation.clickable
import androidx.navigation.NavOptionsBuilder
import com.masterproject.englishapp.components.AppCircularProgress
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.exercises.model.ExerciseType
import com.masterproject.englishapp.navigation.NavigationActions
import com.masterproject.englishapp.navigation.params.ExerciseParams
import com.masterproject.englishapp.components.cardspots.SpotPosition
import com.masterproject.englishapp.components.cardspots.CardSpots
import com.masterproject.englishapp.utils.DummyNavigator

@Composable
fun HomeScreen2(navigator: NavigationActions) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        item {
            HomeHeader()
        }

        item {
            LearningHorizontalSection(navigator)
        }

        item {
            Text(
                text = "Practice",
                style = MaterialTheme.typography.titleMedium
            )
        }

        item {
            PracticeSection(navigator)
        }
    }
}

@Composable
fun HomeScreen(navigator: NavigationActions) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HomeHeader()
        Spacer(modifier = Modifier.height(20.dp))
        LearningHorizontalSection(navigator)
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Practice",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        PracticeSection(navigator, modifier = Modifier.weight(1f))
    }
}

@Preview(
    name = "Xiaomi Redmi 9C",
    device = "spec:width=360dp,height=800dp,dpi=269",
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFEEEEEE
)
@Composable
fun HomeScreenPreview() {
    HomeScreen(DummyNavigator)
}

@Composable
fun HomeHeader() {
    Column {
        Text(
            text = "Ready to learn?",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Start where you left off",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun LearningHorizontalSection(navigator: NavigationActions) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            LearningCard(
                R.drawable.ic_quill_pen_cropped,
                title = "Writing",
                description = "Improve your writing skills",
                progress = 0.9f,
                color = Color(0xFF9B6EF3),
                spotPositions = listOf(
                    SpotPosition(R.drawable.ic_spot_1, x = 10.dp, y = 10.dp, size = 80.dp),
                ),
                onClick = {
                    navigator.navigate(
                        Screen.PRACTICE,
                        ExerciseParams(exerciseType = ExerciseType.WRITE).toQuery()
                    )
                }
            )
        }
        item {
            LearningCard(
                R.drawable.ic_comprehension,
                title = "Comprehension",
                description = "Improve your understanding skills",
                progress = 0.6f,
                color = Color(0xFF4BA3C7),
                spotPositions = listOf(
                    SpotPosition(R.drawable.ic_spot_2, x = (-30).dp, y = (-30).dp, size = 80.dp),
                    SpotPosition(R.drawable.ic_spot_3, x = 60.dp, y = 50.dp, size = 50.dp),
                ),
                onClick = {
                    navigator.navigate(
                        Screen.PRACTICE,
                        ExerciseParams(exerciseType = ExerciseType.COMPREHENSION).toQuery()
                    )
                }
            )
        }
        item {
            LearningCard(
                R.drawable.ic_listening,
                title = "Listening",
                description = "Understand spoken English better",
                progress = 0.3f,
                color = Color(0xFF6FCF97),
                spotPositions = listOf(
                    SpotPosition(R.drawable.ic_spot_1, x = 10.dp, y = 10.dp, size = 60.dp),
                    SpotPosition(R.drawable.ic_spot_2, x = 50.dp, y = 50.dp, size = 50.dp)
                ),
                onClick = {
                    navigator.navigate(
                        Screen.PRACTICE,
                        ExerciseParams(exerciseType = ExerciseType.LISTENING).toQuery()
                    )
                }
            )
        }
    }
}

@Composable
fun LearningCard(
    imageId: Int,
    title: String,
    description: String,
    progress: Float,
    color: Color,
    spotPositions: List<SpotPosition> = emptyList(),
    onClick: () -> Unit = {}
) {
    CardSpots(
        modifier = Modifier
            .width(180.dp)
            .height(190.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(20.dp),
        spotPositions = spotPositions
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppIcon(resId = imageId, size = 80.dp)
                AppCircularProgress(progress = progress)
            }

            Column(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                )
                Text(description, color = Color.White, fontSize = 15.sp)
            }
        }
    }
}

@Composable
fun PracticeSection(navigator: NavigationActions, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PracticeCard(
                R.drawable.ic_speech,
                "Pronunciation",
                description = "Perfect your accent and intonation",
                0.7f
            ) {
                navigator.navigate(
                    Screen.PRACTICE,
                    ExerciseParams(exerciseType = ExerciseType.SPEAK).toQuery()
                )
            }
        }
        item {
            PracticeCard(
                R.drawable.ic_chat,
                "Chat",
                description = "Talk with interactive chat exercises",
                0.5f
            ) {

            }
        }
        item {
            PracticeCard(
                R.drawable.ic_robot_human,
                "Conversation",
                description = "Build real conversational skills",
                0.35f
            ) {

            }
        }
        item {
            PracticeCard(
                R.drawable.ic_camera,
                "Object Identification",
                description = "Discover objets names with camera",
                0.35f
            ) {
                navigator.navigate(Screen.CAMERA)
            }
        }
    }
}

@Composable
fun PracticeSection2(navigator: NavigationActions) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        PracticeCard(
            R.drawable.ic_speech,
            "Pronunciation",
            description = "Perfect your accent and intonation",
            0.7f
        ) {
            navigator.navigate(
                Screen.PRACTICE,
                ExerciseParams(exerciseType = ExerciseType.SPEAK).toQuery()
            )
        }
        PracticeCard(
            R.drawable.ic_chat,
            "Chat",
            description = "Talk with interactive chat exercises",
            0.5f
        ) {

        }
        PracticeCard(
            R.drawable.ic_robot_human,
            "Conversation",
            description = "Build real conversational skills",
            0.35f
        ) {

        }
        PracticeCard(
            R.drawable.ic_camera,
            "Object Identification",
            description = "Discover objets names with camera",
            0.35f
        ) {
            navigator.navigate(Screen.CAMERA)
        }
    }
}

@Composable
fun PracticeCard(
    imageId: Int,
    title: String,
    description: String,
    progress: Float,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Primary // 0xFF2EB872
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppIcon(resId = imageId, size = 60.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Text(
                        text = description,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            ) {
                AppCircularProgress(
                    progress = progress,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}