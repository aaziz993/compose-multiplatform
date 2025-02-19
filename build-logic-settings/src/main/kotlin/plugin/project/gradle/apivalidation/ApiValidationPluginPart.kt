package plugin.project.gradle.apivalidation

import gradle.moduleProperties
import gradle.libs
import plugin.project.BindingPluginPart
import org.gradle.api.Project

internal class ApiValidationPluginPart(override val project: Project) : BindingPluginPart {

    private val doctor by lazy {
        project.moduleProperties.settings.gradle.doctor
    }

    override val needToApply: Boolean by lazy {
        doctor.enabled && project == project.rootProject
    }

    override fun applyAfterEvaluate() = with(project) {
        // The tool allows dumping binary API of a JVM part of a Kotlin library that is public in the sense of Kotlin visibilities and ensures that the public binary API wasn't changed in a way that makes this change binary incompatible.
        plugins.apply(project.libs.plugins.binary.compatibility.validator.get().pluginId)

        applySettings()
    }

    fun applySettings() {
        with(project) {
            configureApiValidationExtension()
        }
    }
}
