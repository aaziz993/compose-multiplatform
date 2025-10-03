package clib.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import clib.presentation.locale.LocalAppLocale
import clib.presentation.locale.customAppLocale
import clib.presentation.theme.LocalAppDensity
import clib.presentation.theme.customAppDensity

@Composable
public fun AppEnvironment(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalAppLocale provides customAppLocale,
        LocalAppDensity provides customAppDensity,
    ) {
        key(customAppLocale, customAppDensity) {
            content()
        }
    }
}
