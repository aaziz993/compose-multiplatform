package plugins.toolchainmanagement.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.plugins.toolchainmanagement.ToolchainManagement
import gradle.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings

@Serializable
internal data class ToolchainManagementSettings(
    override val enabled: Boolean = true,
) : ToolchainManagement, EnabledSettings {

    context(settings: Settings)
    override fun applyTo() =
        settings.pluginManager.withPlugin(settings.libs.plugins.plugin("foojay-resolver-convention").id) {

        }
}
