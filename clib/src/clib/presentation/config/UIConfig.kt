package clib.presentation.config

import androidx.compose.ui.unit.Density
import clib.presentation.appbar.model.AppBar
import clib.presentation.connectivity.model.Connectivity
import clib.presentation.theme.density.model.DensitySerial
import clib.presentation.theme.model.Theme
import io.ktor.http.Url
import klib.data.config.UIConfig
import kotlinx.serialization.Serializable

@Serializable
public data class UIConfig(
    val appBar: AppBar = AppBar(),
    val connectivity: Connectivity = Connectivity(),
    val theme: Theme = Theme(),
    val density: DensitySerial = Density(2f),
    override val startRoute: Url? = null,
    override val authRoute: Url? = null,
    override val authRedirectRoute: Url? = null,
    override val routes: Map<String, RouteConfig> = emptyMap(),
    override val databaseName: String? = null,
) : UIConfig
