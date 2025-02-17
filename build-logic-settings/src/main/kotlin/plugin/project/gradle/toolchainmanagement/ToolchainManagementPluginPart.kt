package plugin.project.gradle.toolchainmanagement

import gradle.amperProjectExtraProperties
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import org.gradle.api.initialization.Settings

internal class ToolchainManagementPluginPart(private val settings: Settings) {

    val needToApply: Boolean by lazy {
        settings.amperProjectExtraProperties.settings.gradle.toolchainManagement.enabled
    }

   init {
        settings.plugins.apply(settings.libs.plugins.plugin("foojay-resolver-convention").id)

        applySettings()
    }

    fun applySettings() = with(settings) {
        configureToolchainManagement()
    }
}
