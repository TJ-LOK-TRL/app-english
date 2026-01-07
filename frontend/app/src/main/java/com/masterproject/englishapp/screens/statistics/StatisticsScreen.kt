package com.masterproject.englishapp.screens.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.components.cards.Rect3DCard
import com.masterproject.englishapp.components.chart.RadarChart
import com.masterproject.englishapp.components.headers.StatisticsHeader
import com.masterproject.englishapp.ui.theme.AppColors

data class StatItem(val label: String, val value: Int, val icon: Int? = null)

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel(),
    goBack: () -> Unit
) {
    val stats = viewModel.user?.statistics
    if (stats == null) {
        goBack()
    } else {
        val radarData = remember(stats) { viewModel.getRadarStats(stats) }
        val totalExercises = remember(stats) { viewModel.getTotalExercises(stats) }
        val totalCorrect = remember(stats) { viewModel.getTotalCorrect(stats) }
        val avgTime = remember(stats) { viewModel.getFormattedAvgTime(stats) }

        val topCards = listOf(
            StatItem("Longest streak", stats.longestStreak, R.drawable.ic_streak),
            StatItem("Current streak", stats.currentStreak, R.drawable.ic_fire),
            StatItem("Learning days", stats.totalLearningDays, R.drawable.ic_calendar)
        )

        val bottomGrid = listOf(
            StatItem("Correct Ex.", totalCorrect),
            StatItem("Incorrect Ex.", totalExercises - totalCorrect),
            StatItem("Total Ex.", totalExercises),
            StatItem("Lessons", stats.lessonsPassed),
            StatItem("Avg Min", avgTime),
            StatItem("Words", viewModel.getWordsDiscoveredCount())
        )

        StatisticsScreenContent(
            topCards = topCards,
            radarData = radarData,
            gridStats = bottomGrid,
            goBack
        )
    }
}

@Composable
fun StatisticsScreenContent(
    topCards: List<StatItem>,
    radarData: Map<String, Float>,
    gridStats: List<StatItem>,
    goBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        StatisticsHeader(
            title = "Statistics",
            onBackClick = goBack
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 0.dp).weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                topCards.forEach { item ->
                    StatsCard(
                        resId = item.icon,
                        number = item.value,
                        description = item.label,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Rect3DCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        text = "Accuracy Area",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.5.dp,
                        color = AppColors.Gray400.copy(alpha = 0.5f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        RadarChart(
                            stats = radarData,
                            color = AppColors.Primary
                        )
                    }
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
                    .height(180.dp)
            ) {
                items(gridStats) { item ->
                    StatsCard(
                        number = item.value,
                        description = item.label
                    )
                }
            }
        }
    }
}

@Composable
fun StatsCard(
    modifier: Modifier = Modifier,
    resId: Int? = null,
    number: Int,
    description: String
) {
    Rect3DCard(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row {
                resId?.let {
                    AppIcon(resId = resId, size = 24.dp)
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = number.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = AppColors.Black800,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Text(
                text = description,
                color = AppColors.Gray500,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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
fun StatisticsScreenContentPreview() {
    val dummyRadarData = remember {
        mapOf(
            "Write" to 0.85f,
            "Speak" to 0.40f,
            "Listening" to 0.70f,
            "Comp." to 0.90f,
            "Grammar" to 0.75f,
            "Vocabulary" to 0.65f
        )
    }

    val dummyTopCards = remember {
        listOf(
            StatItem("Longest streak", 24, R.drawable.ic_streak),
            StatItem("Current streak", 7, R.drawable.ic_fire),
            StatItem("Learning days", 120, R.drawable.ic_calendar)
        )
    }

    val dummyGridStats = remember {
        listOf(
            StatItem("Correct", 450),
            StatItem("Incorrect", 120),
            StatItem("Total Ex.", 570),
            StatItem("Lessons", 85),
            StatItem("Avg Min", 12),
            StatItem("Words", 342)
        )
    }

    StatisticsScreenContent(
        topCards = dummyTopCards,
        radarData = dummyRadarData,
        gridStats = dummyGridStats
    ) { }
}
