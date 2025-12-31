package com.masterproject.englishapp.components.optiongrid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OptionGridLayout(
    modifier: Modifier = Modifier,
    columns: Int,
    itemCount: Int,
    content: @Composable (index: Int) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val rows = (itemCount + columns - 1) / columns

        var index = 0
        repeat(rows) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(columns) {
                    if (index < itemCount) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            content(index)
                        }
                        index++
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}