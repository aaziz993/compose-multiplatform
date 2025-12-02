package klib.data.config

import com.diamondedge.logging.KmLogging
import com.diamondedge.logging.LogLevel
import kotlinx.serialization.Serializable

@Serializable
public data class LogConfig(
    public val level: String? = null,
    override val enabled: Boolean = true,
) : EnabledConfig {

    public fun configureKmLogging(): Unit =
        KmLogging.setLogLevel(
            if (enabled) level?.let {
                LogLevel.valueOf(it.lowercase().replaceFirstChar { it.uppercase() })
            } ?: LogLevel.Info
            else LogLevel.Off,
        )
}
