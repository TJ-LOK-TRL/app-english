package com.masterproject.englishapp.screens.home

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.masterproject.englishapp.components.AppCircularProgress
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.exercises.model.ExerciseType
import com.masterproject.englishapp.navigation.NavigationActions
import com.masterproject.englishapp.navigation.params.ExerciseParams
import com.masterproject.englishapp.components.cardspots.SpotPosition
import com.masterproject.englishapp.components.cardspots.CardSpots
import com.masterproject.englishapp.utils.DummyNavigator

@Composable
fun HomeScreen(
    navigator: NavigationActions,
    viewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreenContent(
        navigator,
        username = viewModel.user?.name ?: "",
        getProgressForType = { type -> viewModel.getProgressForType(type) }
    )
}

@Composable
fun HomeScreenContent(
    navigator: NavigationActions,
    username: String,
    getProgressForType: (ExerciseType) -> Float
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Column(
                modifier = Modifier.width(60.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Hi, $username",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = AppColors.Black800
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Surface(
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        navigator.navigate(Screen.ACCOUNT) {
                            popUpTo(Screen.HOME.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                shape = CircleShape,
                color = AppColors.Primary.copy(alpha = 0.1f),
                border = BorderStroke(2.dp, AppColors.Primary.copy(alpha = 0.5f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = username.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = AppColors.Primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

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
        Spacer(modifier = Modifier.height(20.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                LearningCard(
                    R.drawable.ic_quill_pen_cropped,
                    title = "Writing",
                    description = "Improve your writing skills",
                    progress = getProgressForType(ExerciseType.WRITE),
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
                    progress = getProgressForType(ExerciseType.COMPREHENSION),
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
                    progress = getProgressForType(ExerciseType.LISTENING),
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
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Practice",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                PracticeCard(
                    R.drawable.ic_speech,
                    "Pronunciation",
                    description = "Perfect your accent and intonation",
                    getProgressForType(ExerciseType.SPEAK)
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
                    null
                ) {
                    navigator.navigate(Screen.CHAT)
                }
            }
            item {
                PracticeCard(
                    R.drawable.ic_robot_human,
                    "Conversation",
                    description = "Build real conversational skills",
                    null
                ) {
                    navigator.navigate(Screen.AVATAR)
                }
            }
            item {
                PracticeCard(
                    R.drawable.ic_camera,
                    "Object Identification",
                    description = "Discover objets names with camera",
                    null
                ) {
                    navigator.navigate(Screen.CAMERA)
                }
            }
        }
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
    HomeScreenContent(DummyNavigator, "Tyrese Jerome") {
        0.5f
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
fun PracticeCard(
    imageId: Int,
    title: String,
    description: String,
    progress: Float?,
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
                progress?.let {
                    AppCircularProgress(
                        progress = it,
                        modifier = Modifier.size(32.dp)
                    )
                } ?: run {
                    AppIcon(resId = R.drawable.ic_green_star, size = 32.dp)
                }
            }
        }
    }
}