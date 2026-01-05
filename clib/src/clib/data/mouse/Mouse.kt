package clib.data.mouse

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import clib.presentation.noLocalProvidedFor
import klib.data.mouse.Mouse

@Suppress("ComposeCompositionLocalUsage")
internal val LocalMouse: ProvidableCompositionLocal<Mouse> =
    compositionLocalOf { noLocalProvidedFor("LocalMouse") }
