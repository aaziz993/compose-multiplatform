package plugin.project.gradle.spotless

import gradle.moduleProperties
import gradle.libs
import plugin.project.BindingPluginPart
import org.gradle.api.Project

internal class SpotlessPluginPart(override val project: Project) : BindingPluginPart {

    private val spotless by lazy {
        project.moduleProperties.settings.gradle.spotless
    }

    override val needToApply: Boolean by lazy {
        spotless.enabled
    }

    override fun applyAfterEvaluate() = with(project) {
        super.applyAfterEvaluate()

        plugins.apply(project.libs.plugins.spotless.get().pluginId)

        applySettings()
    }

    fun applySettings() {
        with(project) {
            configureSpotlessExtension()
        }
    }
}
