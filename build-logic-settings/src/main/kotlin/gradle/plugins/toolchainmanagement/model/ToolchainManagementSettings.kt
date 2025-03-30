package gradle.plugins.toolchainmanagement.model

import gradle.plugins.toolchainmanagement.ToolchainManagement
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings

@Serializable
internal class ToolchainManagementSettings : ToolchainManagement {

    context(Settings)
    override fun applyTo() =
        settings.pluginManager.withPlugin("org.gradle.toolchains.foojay-resolver-convention") {

        }
}
