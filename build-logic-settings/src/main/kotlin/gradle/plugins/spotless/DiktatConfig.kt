package gradle.plugins.spotless

import com.diffplug.gradle.spotless.BaseKotlinExtension
import kotlinx.serialization.Serializable

@Serializable
internal data class DiktatConfig(
    val version: String? = null,
    val configFile: String? = null
) {

    fun applyTo(config: BaseKotlinExtension.DiktatConfig) {
        configFile?.let(config::configFile)
    }
}
