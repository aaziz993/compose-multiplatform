package clib.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import androidx.navigationevent.compose.rememberNavigationEventDispatcherOwner

@Composable
public fun BackInterceptionProvider(
    interceptionEnabled: Boolean,
    content: @Composable () -> Unit
) {
    val parentOwner = LocalNavigationEventDispatcherOwner.current

    if (interceptionEnabled && parentOwner != null) {
        val interceptingOwner =
            rememberNavigationEventDispatcherOwner(false, parentOwner)

        CompositionLocalProvider(
            LocalNavigationEventDispatcherOwner provides interceptingOwner,
            content = content,
        )
    } else content()
}
