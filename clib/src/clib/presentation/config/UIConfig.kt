package clib.presentation.config

import androidx.compose.ui.unit.Density
import clib.presentation.quickaccess.QuickAccess
import clib.presentation.theme.density.model.DensitySerial
import clib.presentation.theme.model.Theme
import klib.data.config.RouteConfig
import klib.data.config.UIConfig
import klib.data.location.locale.Locale
import klib.data.location.locale.current
import kotlinx.serialization.Serializable

@Serializable
public data class UIConfig(
    val theme: Theme = Theme(),
    val density: DensitySerial = Density(2f),
    val locales: List<Locale> = emptyList(),
    val locale: Locale = Locale.current,
    val quickAccess: QuickAccess = QuickAccess(),
    override val startRoute: String? = null,
    override val authRoute: String? = null,
    override val authRedirectRoute: String? = null,
    override val routes: Map<String, RouteConfig> = emptyMap(),
    override val databaseName: String? = null,
) : UIConfig
