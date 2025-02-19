package plugin.project.kotlin.atomicfu

import gradle.moduleProperties
import gradle.libs
import plugin.project.BindingPluginPart
import org.gradle.api.Project

internal class AtomicFUPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply: Boolean by lazy {
        project.moduleProperties.settings.kotlin.atomicFU.enabled
    }

    override fun applyAfterEvaluate() = with(project) {
        plugins.apply(project.libs.plugins.atomicfu.get().pluginId)

        applySettings()
    }

    private fun applySettings() {
        with(project) {
            configureAtomicFUPluginExtension()
        }
    }
}
