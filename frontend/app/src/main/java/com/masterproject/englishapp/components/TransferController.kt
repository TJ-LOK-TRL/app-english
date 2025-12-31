package com.masterproject.englishapp.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberTransferController(): TransferController {
    val scope = rememberCoroutineScope()
    return remember { TransferController(scope) }
}

class TransferController(private val scope: CoroutineScope) {

    // Estado do ghost
    var ghostContent: (@Composable () -> Unit)? by mutableStateOf(null)
    var ghostOffset by mutableStateOf(Offset.Zero)

    private var ghostAnim = Animatable(Offset.Zero, Offset.VectorConverter)

    fun animateTransfer(
        from: Offset,
        to: Offset,
        duration: Int = 350,
        content: @Composable () -> Unit,
        onEnd: () -> Unit,
    ) {
        ghostContent = content
        ghostOffset = from
        ghostAnim = Animatable(from, Offset.VectorConverter)

        scope.launch {
            ghostAnim.animateTo(to, tween(duration))
            ghostOffset = to
            ghostContent = null
            onEnd()
        }
    }

    @Composable
    fun GhostLayer() {
        val density = LocalDensity.current
        val offset = ghostOffset

        ghostContent?.let { content ->
            Box(
                modifier = Modifier.absoluteOffset(
                    x = with(density) { offset.x.toDp() },
                    y = with(density) { offset.y.toDp() }
                )
            ) {
                content()
            }
        }
    }
}