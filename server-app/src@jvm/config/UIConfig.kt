package config

import klib.data.config.RouteConfig
import klib.data.config.UIConfig

public data class UIConfig(
    override val startRoute: String? = null,
    override val authRedirectRoute: String? = null,
    override val authRoute: String? = null,
    override val routes: Map<String, RouteConfig> = emptyMap(),
    override val databaseName: String? = null,
) : UIConfig
