package plugins.toolchainmanagement

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import plugins.toolchainmanagement.model.ToolchainManagementSettings

internal class ToolchainManagementPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        with(target) {
            projectProperties.plugins.toolchainManagement
                .takeIf(ToolchainManagementSettings::enabled)?.let { toolchainManagement ->
                    plugins.apply(libs.plugins.plugin("foojayResolverConvention").id)

                    toolchainManagement.applyTo()
                }
        }
    }
}
