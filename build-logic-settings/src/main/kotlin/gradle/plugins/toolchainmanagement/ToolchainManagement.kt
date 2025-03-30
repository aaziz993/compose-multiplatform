package gradle.plugins.toolchainmanagement

import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings

@Serializable
internal class ToolchainManagement {

    context(Settings)
    @Suppress("UnstableApiUsage")
    fun applyTo() =
        settings.pluginManager.withPlugin("org.gradle.toolchains.foojay-resolver-convention") {
            settings.toolchainManagement {

            }
        }
}
