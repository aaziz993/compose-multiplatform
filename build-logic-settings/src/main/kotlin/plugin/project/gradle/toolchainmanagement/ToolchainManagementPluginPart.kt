package plugin.project.gradle.toolchainmanagement

import gradle.amperModuleExtraProperties
import gradle.amperProjectExtraProperties
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import org.gradle.api.initialization.Settings
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx

internal class ToolchainManagementPluginPart(private val settings: Settings) {

    val needToApply: Boolean by lazy {
        settings.amperProjectExtraProperties.settings.gradle.toolchain.enabled
    }

   init {
        settings.plugins.apply(settings.libs.plugins.plugin("foojay-resolver-convention").id)

        applySettings()
    }

    fun applySettings() = with(settings) {
        configureToolchainManagement()
    }
}
