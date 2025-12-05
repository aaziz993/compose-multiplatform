package clib.presentation

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.toIntRect

/**
 * Adds a stroke to the bottom of a Composable.
 *
 * @param color The color of the stroke.
 * @param strokeWidth The width of the stroke.
 */
public fun Modifier.bottomStroke(color: Color, strokeWidth: Dp): Modifier =
    then(
        drawWithContent {
            drawContent()
            drawLine(
                color = color,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = strokeWidth.toPx(),
            )
        },
    )

/**
 * Applies a fading edge effect to a composable using the given [brush].
 *
 * The content is drawn normally, then masked with the [brush] using
 * [BlendMode.DstIn], creating a smooth fade at the edges.
 *
 * @param brush The [Brush] defining the fade gradient.
 */
public fun Modifier.fadingEdge(brush: Brush): Modifier =
    graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()
            drawRect(brush = brush, blendMode = BlendMode.DstIn)
        }

/**
 * This custom modifier is specifically designed for a LazyVerticalGrid layout. Consequently, it leverages the unique properties of LazyGridState and other grid-specific parameters to enable precise item selection and interaction within the grid’s context.
 *
 * This extension function multiSelectGridHandler enhances the pointerInput modifier for a lazy grid with multi-select drag functionality. Key features include:
 *
 * Grid Item Detection:
 *
 * gridItemKeyAtPosition finds the grid item at a specific touch point.
 * Uses item’s layout information to determine precise location.
 * Long Press Selection:
 *
 * Starts selection on long press.
 * Triggers haptic feedback.
 * Adds first selected item to selectedIds.
 * Drag Selection:
 *
 * Allows selecting multiple items by dragging.
 * Dynamically updates selectedIds set.
 * Supports range selection between initial and current items.
 * Auto-Scrolling:
 *
 * Calculates scroll speed based on proximity to grid edges.
 * Enables smooth scrolling during selection.
 * Adjusts autoScrollSpeed dynamically.
 * The function provides a sophisticated, custom interaction handler for grid-based selection scenarios.
 */
public fun Modifier.multiSelectGridHandler(
    lazyGridState: LazyGridState,
    haptics: HapticFeedback,
    selectedIds: MutableState<Set<Long>>,
    autoScrollSpeed: MutableState<Float>,
    autoScrollThreshold: Float
): Modifier = pointerInput(Unit) {

    fun LazyGridState.gridItemKeyAtPosition(hitPoint: Offset): Long? =
        layoutInfo.visibleItemsInfo.find { itemInfo ->
            itemInfo
                .size
                .toIntRect()
                .contains(hitPoint.round() - itemInfo.offset)
        }?.key as? Long

    var initialKey: Long? = null
    var currentKey: Long? = null
    detectDragGesturesAfterLongPress(
        onDragStart = { offset ->
            lazyGridState.gridItemKeyAtPosition(offset)?.let { key ->
                if (!selectedIds.value.contains(key)) {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    initialKey = key
                    currentKey = key
                    selectedIds.value += key
                }
            }
        },
        onDragCancel = { initialKey = null; autoScrollSpeed.value = 0f },
        onDragEnd = { initialKey = null; autoScrollSpeed.value = 0f },
        onDrag = { change, _ ->
            if (initialKey != null) {
                val distFromBottom =
                    lazyGridState.layoutInfo.viewportSize.height - change.position.y
                val distFromTop = change.position.y

                autoScrollSpeed.value = when {
                    distFromBottom < autoScrollThreshold
                        -> autoScrollThreshold - distFromBottom

                    distFromTop < autoScrollThreshold
                        -> -(autoScrollThreshold - distFromTop)

                    else -> 0f
                }

                lazyGridState.gridItemKeyAtPosition(change.position)?.let { key ->
                    if (currentKey != key) {
                        selectedIds.value = selectedIds.value
                            .minus(initialKey!!..currentKey!!)
                            .minus(currentKey!!..initialKey!!)
                            .plus(initialKey!!..key)
                            .plus(key..initialKey!!)
                        currentKey = key
                    }
                }
            }
        },
    )
}
