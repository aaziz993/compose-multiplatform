package plugins.toolchainmanagement.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.plugins.toolchainmanagement.ToolchainManagement
import gradle.plugins.project.EnabledSettings
import gradle.accessors.plugin
import gradle.accessors.plugins
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings

@Serializable
internal data class ToolchainManagementSettings(
    override val enabled: Boolean = true,
) : ToolchainManagement, EnabledSettings {

    context(Settings)
    override fun applyTo() =
        pluginManager.withPlugin(libs.plugins.plugin("foojay-resolver-convention").id) {

        }
}
