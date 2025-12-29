package com.example.core_ui.components.toggle

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun GurukulSegmentedToggle(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val horizontalPadding = 14.dp
    val height = 32.dp

    // store x-position & text width (NOT padded width)
    val itemPositions = remember { mutableStateMapOf<String, Pair<Int, Int>>() }

    val selected = itemPositions[selectedItem]

    val offsetX by animateFloatAsState(
        targetValue = selected?.first?.toFloat() ?: 0f,
        animationSpec = tween(300),
        label = "offset"
    )

    val indicatorWidth by animateFloatAsState(
        targetValue = selected?.second?.toFloat() ?: 0f,
        animationSpec = tween(300),
        label = "width"
    )

    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(2.dp)
    ) {

        // 🔵 Indicator (BEHIND text)
        if (selected != null) {
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (offsetX - with(density) { horizontalPadding.toPx() }).toInt(),
                            0
                        )
                    }
                    .width(
                        with(density) {
                            (indicatorWidth + horizontalPadding.toPx() * 2).toDp()
                        }
                    )
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .zIndex(0f)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.zIndex(1f)
        ) {
            items.forEach { item ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = horizontalPadding)
                        .onGloballyPositioned { coordinates ->
                            val xPosition = coordinates.positionInParent().x.toInt()
                            val width = coordinates.size.width
                            itemPositions[item] = xPosition to width
                        }
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onItemSelected(item)
                        }
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (item == selectedItem)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
@Composable
private fun ToggleItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = text,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primary
                else Color.Transparent
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        color = if (selected)
            MaterialTheme.colorScheme.onPrimary
        else
            MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.labelMedium
    )
}