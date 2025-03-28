package gradle.plugins.toolchainmanagement

import gradle.accessors.id
import gradle.accessors.catalog.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import org.gradle.api.initialization.Settings

internal interface ToolchainManagement {

    context(Settings)
    @Suppress("UnstableApiUsage")
    fun applyTo() =
        settings.pluginManager.withPlugin(settings.libs.plugin("foojayResolverConvention").id) {
            settings.toolchainManagement {

            }
        }
}
