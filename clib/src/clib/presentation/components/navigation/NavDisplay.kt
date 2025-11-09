package clib.presentation.components.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.defaultPopTransitionSpec
import androidx.navigation3.ui.defaultPredictivePopTransitionSpec
import androidx.navigation3.ui.defaultTransitionSpec
import androidx.navigationevent.NavigationEvent

@Composable
public fun NavDisplay(
    backStack: List<Route>,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    onBack: () -> Unit = {
        if (backStack is MutableList<Route>) backStack.removeLastOrNull()
    },
    entryDecorators: List<NavEntryDecorator<Route>> = listOf(
        rememberSaveableStateHolderNavEntryDecorator()
    ),
    sceneStrategy: SceneStrategy<Route> = SinglePaneSceneStrategy(),
    sizeTransform: SizeTransform? = null,
    transitionSpec: AnimatedContentTransitionScope<Scene<Route>>.() -> ContentTransform = defaultTransitionSpec(),
    popTransitionSpec: AnimatedContentTransitionScope<Scene<Route>>.() -> ContentTransform = defaultPopTransitionSpec(),
    predictivePopTransitionSpec:
    AnimatedContentTransitionScope<Scene<Route>>.(@NavigationEvent.SwipeEdge Int) -> ContentTransform =
        defaultPredictivePopTransitionSpec(),
    metadata: (Route) -> Map<String, Any> = { emptyMap() },
): Unit = NavDisplay(
    backStack,
    modifier,
    contentAlignment,
    onBack,
    entryDecorators,
    sceneStrategy,
    sizeTransform,
    transitionSpec,
    popTransitionSpec,
    predictivePopTransitionSpec,
) { key -> key.navRoute.entry(key, metadata(key)) }
