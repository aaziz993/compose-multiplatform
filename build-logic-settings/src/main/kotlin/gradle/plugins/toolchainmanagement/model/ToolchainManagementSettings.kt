package gradle.plugins.toolchainmanagement.model

import gradle.accessors.id
import gradle.accessors.catalog.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.plugins.project.EnabledSettings
import gradle.plugins.toolchainmanagement.ToolchainManagement
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings

@Serializable
internal data class ToolchainManagementSettings(
    override val enabled: Boolean = true,
) : ToolchainManagement, EnabledSettings {

    context(Settings)
    override fun applyTo() =
        settings.pluginManager.withPlugin(settings.libs.plugin("foojayResolverConvention").id) {

        }
}
