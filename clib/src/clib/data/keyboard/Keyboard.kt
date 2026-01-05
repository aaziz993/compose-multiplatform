package clib.data.keyboard

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import clib.presentation.noLocalProvidedFor
import klib.data.keyboard.Keyboard

@Suppress("ComposeCompositionLocalUsage")
internal val LocalKeyboard: ProvidableCompositionLocal<Keyboard> =
    compositionLocalOf { noLocalProvidedFor("LocalKeyboard") }
