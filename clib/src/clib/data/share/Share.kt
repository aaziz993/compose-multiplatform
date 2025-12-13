package clib.data.share

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.noLocalProvidedFor
import klib.data.share.Share

@Suppress("ComposeCompositionLocalUsage")
public val LocalShare: ProvidableCompositionLocal<Share> =
    staticCompositionLocalOf { noLocalProvidedFor("LocalShare") }

@Composable
public expect fun rememberShare(): Share
