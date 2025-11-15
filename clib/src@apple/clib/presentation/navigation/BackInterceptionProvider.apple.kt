package clib.presentation.navigation

import androidx.compose.runtime.Composable

@Composable
public actual fun BackInterceptionProvider(
    interceptionEnabled: Boolean,
    content: @Composable () -> Unit
): Unit = content()
