package clib.presentation.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.noLocalProvidedFor
import klib.data.location.locale.Locale

@Suppress("ComposeCompositionLocalUsage")
public val LocalLocaleState: ProvidableCompositionLocal<LocaleState> =
    staticCompositionLocalOf { noLocalProvidedFor("LocalLocaleState") }

public class LocaleState(initialValue: Locale? = null) {

    public var locale: Locale? by mutableStateOf(initialValue)

    public companion object Companion {

        public val Saver: Saver<LocaleState, *> = listSaver(
            save = { listOf(it.locale) },
            restore = { LocaleState(it[0]) },
        )
    }
}

@Composable
public fun rememberLocaleState(initialValue: Locale? = null): LocaleState =
    rememberSaveable(saver = LocaleState.Saver) { LocaleState(initialValue) }
