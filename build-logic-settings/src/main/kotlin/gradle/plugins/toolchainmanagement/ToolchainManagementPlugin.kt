package gradle.plugins.toolchainmanagement

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.plugins.toolchainmanagement.model.ToolchainManagementSettings
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

internal class ToolchainManagementPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        with(target) {
            projectProperties.plugins.toolchainManagement
                .takeIf(ToolchainManagementSettings::enabled)?.let { toolchainManagement ->
                    plugins.apply(libs.plugin("foojayResolverConvention").id)

                    toolchainManagement.applyTo()
                }
        }
    }
}
