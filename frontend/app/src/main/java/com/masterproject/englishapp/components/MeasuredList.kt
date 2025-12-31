package com.masterproject.englishapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <T> MeasuredList(
    displayList: List<T>,
    fullList: List<T>,
    howCreate: @Composable (Int, T) -> Unit,
    horizontalSpacing: Dp = 5.dp,
    lineSpacing: Dp = 10.dp,
    lineOffset: Dp = 4.dp,
    backgroundContent: @Composable (lineYs: List<Int>, totalHeight: Int) -> Unit = { _, _ -> }
) {
    var totalHeightPx by remember { mutableIntStateOf(0) }
    var lineYs by remember { mutableStateOf(emptyList<Int>()) }

    Box(modifier = Modifier.fillMaxWidth()) {

        backgroundContent(lineYs, totalHeightPx)

        SubcomposeLayout { constraints ->
            val allPlaceables = fullList.mapIndexed { index, item ->
                subcompose("measure-$index") {
                    howCreate(index, item)
                }.first().measure(constraints)
            }

            val (measuredHeight, numberOfLines, linePositionsY) = forEachFlowRowItem(
                allPlaceables,
                constraints.maxWidth,
                horizontalSpacing.roundToPx(),
                lineSpacing.roundToPx(),
                lineOffsetPx = lineOffset.roundToPx()
            )

            totalHeightPx = measuredHeight
            lineYs = linePositionsY

            val visiblePlaceables = displayList.mapIndexed { index, item ->
                subcompose("visible-$index") {
                    howCreate(index, item)
                }.first().measure(constraints)
            }

            layout(constraints.maxWidth, totalHeightPx) {
                forEachFlowRowItem(
                    visiblePlaceables,
                    constraints.maxWidth,
                    horizontalSpacing.roundToPx(),
                    lineSpacing.roundToPx(),
                    lineOffsetPx = lineOffset.roundToPx(),
                    onPlace = { placeable, x, y -> placeable.place(x, y) }
                )
            }
        }

        //val totalHeightDp = with(LocalDensity.current) { totalHeightPx.toDp() }
        //Text("Altura total (todos elementos): $totalHeightDp")
    }
}

fun forEachFlowRowItem(
    placeables: List<Placeable>,
    maxWidth: Int,
    horizontalSpacingPx: Int,
    lineSpacingPx: Int,
    lineOffsetPx: Int,
    onPlace: (placeable: Placeable, x: Int, y: Int) -> Unit = { _, _, _ -> },
    onNewLine: (() -> Unit)? = null
): Triple<Int, Int, List<Int>> { // returns totalHeight and numberOfLines
    var xPos = 0
    var yPos = 0
    var lineHeight = 0
    val linePositionsY = mutableListOf<Int>()
    var totalHeight = 0
    var lineCount = 1

    placeables.forEach { placeable ->
        // Break line if line is full
        if (xPos + placeable.width > maxWidth) {
            linePositionsY.add(yPos + lineHeight + lineOffsetPx)
            xPos = 0
            yPos += lineHeight + lineSpacingPx
            totalHeight += lineHeight + lineSpacingPx
            lineHeight = 0
            lineCount++
            onNewLine?.invoke()
        }
        // Place the item at the current position
        onPlace(placeable, xPos, yPos)
        xPos += placeable.width + horizontalSpacingPx
        lineHeight = maxOf(lineHeight, placeable.height)
    }

    totalHeight += lineHeight // Last line
    linePositionsY.add(yPos + lineHeight + lineOffsetPx)
    return Triple(totalHeight, lineCount, linePositionsY)
}