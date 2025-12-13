package clib.presentation.cache

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import klib.data.cache.Cache
import klib.data.cache.emptyCache

@Suppress("ComposeCompositionLocalUsage")
public val LocalCache: ProvidableCompositionLocal<Cache<String, Any>> =
    staticCompositionLocalOf(::emptyCache)
