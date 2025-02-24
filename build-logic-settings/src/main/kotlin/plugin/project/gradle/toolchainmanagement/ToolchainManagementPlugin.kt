package plugin.project.gradle.toolchainmanagement

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import plugin.project.gradle.toolchainmanagement.model.ToolchainManagementSettings

internal class ToolchainManagementPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        with(target) {
            projectProperties.plugins.toolchainManagement
                .takeIf(ToolchainManagementSettings::enabled)?.let { toolchainManagement ->
                    plugins.apply(settings.libs.plugins.plugin("foojay-resolver-convention").id)

                    toolchainManagement.applyTo()
                }
        }
    }
}
