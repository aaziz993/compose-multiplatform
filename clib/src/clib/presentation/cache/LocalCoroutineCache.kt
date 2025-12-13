package clib.presentation.cache

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import klib.data.cache.CoroutineCache
import klib.data.cache.emptyCoroutineCache

@Suppress("ComposeCompositionLocalUsage")
public val LocalCoroutineCache: ProvidableCompositionLocal<CoroutineCache<String, Any>> =
    staticCompositionLocalOf(::emptyCoroutineCache)
