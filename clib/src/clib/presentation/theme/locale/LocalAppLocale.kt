@file:Suppress("ComposeCompositionLocalUsage")

package clib.presentation.theme.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import klib.data.location.locale.Locale
import org.jetbrains.compose.resources.ResourceItem
import org.jetbrains.compose.resources.StringResource

public var customAppLocale: Locale? by mutableStateOf(null)

public expect object LocalAppLocale {

    public val current: Locale @Composable get

    @Composable
    public infix fun provides(value: Locale?): ProvidedValue<*>
}

@Composable
public fun stringResource(res: String): StringResource = StringResource(
    "string:$res",
    res,
    setOf(
        ResourceItem(setOf(), "values${LocalAppLocale.current}/strings.xml", -1, -1),
    ),
)
