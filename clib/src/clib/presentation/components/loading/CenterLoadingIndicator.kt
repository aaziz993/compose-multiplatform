package clib.presentation.components.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Suppress("ComposeModifierMissing")
@Composable
public fun CenterLoadingIndicator(): Unit = Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    LoadingIndicator()
}
