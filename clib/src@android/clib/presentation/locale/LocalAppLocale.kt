@file:SuppressLint("LocalContextConfigurationRead")

package clib.presentation.locale

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import klib.data.location.locale.Locale
import klib.data.location.locale.current
import klib.data.location.locale.setCurrent
import klib.data.location.locale.toJavaLocale
import klib.data.type.serialization.plus

internal actual object LocalAppLocale {

    actual val current: Locale
        @Composable get() = Locale.current

    @Composable
    actual infix fun provides(value: Locale): ProvidedValue<*> {
        Locale.setCurrent(value)

        val context = LocalContext.current
        val configuration = Configuration(context.resources.configuration).apply {
            setLocale(value.toJavaLocale())
        }

        val localizedContext = remember(value) {
            context.createConfigurationContext(configuration)
        }

        return LocalConfiguration.provides(configuration).plus(
            LocalContext.provides(localizedContext),
        )
    }
}
