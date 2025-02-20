package plugin.project.kotlin.serialization

import gradle.moduleProperties
import gradle.libs
import plugin.project.BindingPluginPart
import org.gradle.api.Project

internal class SerializationPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply: Boolean by lazy {
        project.moduleProperties.settings.kotlin.serialization.enabled
    }

    override fun applyAfterEvaluate() {
        with(project) {
            plugins.apply(project.libs.plugins.kotlin.serialization.get().pluginId)
        }
    }
}
