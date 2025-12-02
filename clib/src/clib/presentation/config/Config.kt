package clib.presentation.config

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Density
import clib.presentation.noLocalProvidedFor
import clib.presentation.quickaccess.QuickAccess
import clib.presentation.theme.density.model.DensitySerial
import clib.presentation.theme.model.Theme
import klib.data.config.client.RouteConfig
import klib.data.config.client.UIConfig
import klib.data.location.locale.Locale
import klib.data.location.locale.current
import kotlinx.serialization.Serializable

@Suppress("ComposeCompositionLocalUsage")
public val LocalConfig: ProvidableCompositionLocal<Config> =
    staticCompositionLocalOf { noLocalProvidedFor("LocalConfig") }

@Serializable
public data class Config(
    val theme: Theme = Theme(),
    val density: DensitySerial = Density(2f),
    val locales: List<Locale> = emptyList(),
    val locale: Locale = Locale.current,
    val quickAccess: QuickAccess = QuickAccess(),
    override val startRoute: String? = null,
    override val databaseName: String? = null,
    override val authRoute: String? = null,
    override val authRedirectRoute: String? = null,
    override val routes: Map<String, RouteConfig> = emptyMap(),
) : UIConfig
