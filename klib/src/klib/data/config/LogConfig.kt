package klib.data.config

import com.diamondedge.logging.FixedLogLevel
import com.diamondedge.logging.KmLogging
import com.diamondedge.logging.LogLevel
import com.diamondedge.logging.PrintLogger
import kotlinx.serialization.Serializable

@Serializable
public data class LogConfig(
    public val level: String? = null,
    override val enabled: Boolean = true,
) : EnabledConfig {

    public fun configureKmLogging() {
        KmLogging.setLogLevel(
            if (enabled) level?.let {
                LogLevel.valueOf(it.lowercase().replaceFirstChar { it.uppercase() })
            } ?: LogLevel.Info
            else LogLevel.Off,
        )
        KmLogging.setLoggers(PrintLogger(FixedLogLevel(true)))
    }
}
