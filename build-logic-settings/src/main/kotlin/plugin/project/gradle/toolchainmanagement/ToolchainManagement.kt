package plugin.project.gradle.toolchainmanagement

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import org.gradle.api.initialization.Settings

@Suppress("UnstableApiUsage")
internal fun Settings.configureToolchainManagement() =
    pluginManager.withPlugin(libs.plugins.plugin("foojay-resolver-convention").id) {
        projectProperties.settings.gradle.toolchainManagement.let { toolchainManagement ->
            toolchainManagement(toolchainManagement::applyTo)
        }
    }

