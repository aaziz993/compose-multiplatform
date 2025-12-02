package klib.data.config.di

import klib.data.config.EnabledConfig
import klib.data.config.LogConfig
import kotlinx.serialization.Serializable

@Serializable
public data class KoinConfig(
    val logging: LogConfig? = null,
    override val enabled: Boolean,
) : EnabledConfig
