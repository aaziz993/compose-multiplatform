package clib.presentation.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation3.ui.NavDisplay

public fun slideTransition(): Map<String, Any> = NavDisplay.transitionSpec {
    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
} + NavDisplay.popTransitionSpec {
    slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
} + NavDisplay.predictivePopTransitionSpec {
    slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
}
