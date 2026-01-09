@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package clib.presentation.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.rememberGraphicsLayer
import io.github.themeanimator.ThemeAnimationState
import io.github.themeanimator.themeAnimation

@Suppress("ComposeModifierMissing")
@Composable
public fun ThemeAnimationScope(
    state: ThemeAnimationState,
    isDark: Boolean,
    content: @Composable () -> Unit,
) {
    val graphicsLayer = rememberGraphicsLayer()

    Box(
        modifier = Modifier.themeAnimation(
            state = state,
            isDark = isDark,
            graphicsLayer = graphicsLayer,
        ),
    ) {
        content()
    }
}
