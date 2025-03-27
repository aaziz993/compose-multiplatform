package gradle.plugins.toolchainmanagement

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import org.gradle.api.initialization.Settings

internal interface ToolchainManagement {

    context(Settings)
    @Suppress("UnstableApiUsage")
    fun applyTo() =
        settings.pluginManager.withPlugin(settings.libs.plugins.plugin("foojayResolverConvention").id) {
            settings.toolchainManagement {

            }
        }
}
