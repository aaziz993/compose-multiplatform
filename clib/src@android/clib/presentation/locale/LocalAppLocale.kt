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

public actual object LocalAppLocale {

    private var default: Locale? = null
    public actual val current: Locale
        @Composable get() = Locale.current

    @Composable
    public actual infix fun provides(value: Locale?): ProvidedValue<*> {
        val newLocale = value ?: default ?: Locale.current
        if (default == null) default = newLocale

        Locale.setCurrent(newLocale)

        val context = LocalContext.current
        val configuration = Configuration(context.resources.configuration).apply {
            setLocale(newLocale.toJavaLocale())
        }

        val localizedContext = remember(newLocale) {
            context.createConfigurationContext(configuration)
        }

        return LocalConfiguration.provides(configuration).plus(
            LocalContext.provides(localizedContext),
        )
    }
}
