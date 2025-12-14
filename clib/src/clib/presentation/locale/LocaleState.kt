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
import androidx.compose.ui.platform.LocalInspectionMode
import klib.data.location.locale.Locale
import klib.data.location.locale.current

@Suppress("ComposeCompositionLocalUsage")
public val LocalLocaleState: ProvidableCompositionLocal<LocaleState> =
    staticCompositionLocalOf(::LocaleState)

public class LocaleState(initialValue: Locale = Locale.current) {

    public var value: Locale by mutableStateOf(initialValue)

    @Suppress("ComposeUnstableReceiver")
    @Composable
    public fun localeInspectionAware(): Locale =
        (if (LocalInspectionMode.current) null else value) ?: Locale.forLanguageTag("en-US")

    public companion object Companion {

        public val Saver: Saver<LocaleState, *> = listSaver(
            save = { listOf(it.value) },
            restore = { LocaleState(it[0]) },
        )
    }
}

@Composable
public fun rememberLocaleState(initialValue: Locale = Locale.current): LocaleState =
    rememberSaveable(saver = LocaleState.Saver) { LocaleState(initialValue) }
