package clib.presentation.navigation

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
public actual fun systemOnBack(): () -> Unit {
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    return remember(backPressedDispatcher) {
        { backPressedDispatcher?.onBackPressed() }
    }
}
