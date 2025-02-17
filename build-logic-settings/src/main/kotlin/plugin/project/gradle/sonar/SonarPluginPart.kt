package plugin.project.gradle.sonar

import gradle.amperModuleExtraProperties
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx

internal class SonarPluginPart(ctx: PluginPartCtx) : BindingPluginPart by ctx {

    override val needToApply: Boolean by lazy {
        project.amperModuleExtraProperties.settings.gradle.sonar.enabled
    }

    override fun applyAfterEvaluate() {
        super.applyAfterEvaluate()

        project.plugins.apply(project.libs.plugins.sonarqube.get().pluginId)

        applySettings()
    }

    fun applySettings() = with(project) {
        configureSonarExtension()
    }
}
