package com.masterproject.englishapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.components.animations.animatePlacement
import com.masterproject.englishapp.components.animations.rememberTransferController
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@Composable
fun OrderSentenceScreen(
    portugueseSentence: String = "Eu estou à espera do autocarro",
    englishParts: List<String> = listOf("I", "am", "waiting", "for", "the", "bus").shuffled()
) {
    var selectedParts by remember { mutableStateOf(listOf<String>()) }
    val scope = rememberCoroutineScope()

    val constructionPositions = remember { mutableStateMapOf<String, Offset>() }
    val shuffledPoolPositions = remember { mutableStateMapOf<String, Offset>() }

    val transfer = rememberTransferController()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {

        Text(text = portugueseSentence, fontSize = 20.sp)

        // ----------------------------
        // Construction line (FlowRow)
        // ----------------------------
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selectedParts.forEach { part ->
                key(part) {
                    Box(
                        modifier = Modifier
                            .onGloballyPositioned {
                                constructionPositions[part] = it.positionOnScreen()
                                shuffledPoolPositions.remove(part)
                            }
                            .animatePlacement()
                            .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                            .clickable {
                                selectedParts = selectedParts - part

                                val fromPosition = constructionPositions[part]
                                if (fromPosition != null) {
                                    scope.launch {
                                        val toPosition = snapshotFlow { shuffledPoolPositions[part] }
                                            .filterNotNull()
                                            .first()

                                        transfer.animateTransfer(
                                            from = fromPosition,
                                            to = toPosition,
                                            content = { PartBox(part) },
                                            onEnd = { }
                                        )
                                    }
                                }
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = part, fontSize = 16.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --------------------------------
        // Shuffled pool (FlowRow)
        // --------------------------------
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            englishParts.filter { it !in selectedParts }.forEach { part ->
                key(part) {
                    Box(
                        modifier = Modifier
                            .onGloballyPositioned {
                                shuffledPoolPositions[part] = it.positionOnScreen()
                                constructionPositions.remove(part)
                            }
                            .animatePlacement()
                            .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                            .clickable {
                                selectedParts = selectedParts + part

                                val fromPosition = shuffledPoolPositions[part]
                                if (fromPosition != null) {
                                    scope.launch {
                                        val toPosition = snapshotFlow { constructionPositions[part] }
                                            .filterNotNull()
                                            .first()

                                        transfer.animateTransfer(
                                            from = fromPosition,
                                            to = toPosition,
                                            content = { PartBox(part) },
                                            onEnd = { }
                                        )
                                    }
                                }
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = part, fontSize = 16.sp)
                    }
                }
            }
        }

        transfer.GhostLayer()
    }
}

@Composable
fun PartBox(text: String) {
    Box(
        modifier = Modifier
            .border(1.dp, Color.White, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontSize = 16.sp)
    }
}