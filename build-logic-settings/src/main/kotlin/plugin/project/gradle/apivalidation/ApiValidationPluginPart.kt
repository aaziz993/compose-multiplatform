package plugin.project.gradle.apivalidation

import gradle.amperModuleExtraProperties
import gradle.libs
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx

internal class ApiValidationPluginPart(ctx: PluginPartCtx) : BindingPluginPart by ctx {

    private val doctor by lazy {
        project.amperModuleExtraProperties.settings.gradle.doctor
    }

    override val needToApply: Boolean by lazy {
        doctor.enabled && project == project.rootProject
    }

    override fun applyAfterEvaluate() {
        super.applyAfterEvaluate()

        // The tool allows dumping binary API of a JVM part of a Kotlin library that is public in the sense of Kotlin visibilities and ensures that the public binary API wasn't changed in a way that makes this change binary incompatible.
        project.plugins.apply(project.libs.plugins.binary.compatibility.validator.get().pluginId)

        applySettings()
    }

    fun applySettings() = with(project) {
        configureApiValidationExtension()
    }
}
