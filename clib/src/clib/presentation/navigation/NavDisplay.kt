package clib.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay

public val AnimatedContentTransitionScope<Scene<*>>.isPush: Boolean
    get() = targetState.key != initialState.key

public fun slideTransition(): Map<String, Any> = NavDisplay.transitionSpec {
    if (isPush) pushSlide() else popSlide()
} + NavDisplay.popTransitionSpec {
    popSlide()
} + NavDisplay.predictivePopTransitionSpec {
    popSlide()
}

private fun AnimatedContentTransitionScope<Scene<*>>.pushSlide(): ContentTransform? =
    slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(300),
    ) + fadeIn() togetherWith
        slideOutHorizontally(
            targetOffsetX = { -it / 2 },
            animationSpec = tween(300),
        ) + fadeOut()

private fun AnimatedContentTransitionScope<Scene<*>>.popSlide(): ContentTransform? =
    slideInHorizontally(
        initialOffsetX = { -it / 2 },
        animationSpec = tween(300),
    ) + fadeIn() togetherWith
        slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(300),
        ) + fadeOut()
