package plugin.project.kotlin.ksp

import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx
import gradle.amperModuleExtraProperties
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings

internal class KspPluginPart(ctx: PluginPartCtx) : BindingPluginPart by ctx {

    private val doctor by lazy {
        project.amperModuleExtraProperties.settings.gradle.doctor
    }

    override val needToApply: Boolean by lazy {
        doctor.enabled && project == project.rootProject
    }

    override fun applyBeforeEvaluate() {
        project.plugins.apply(project.settings.libs.plugins.plugin("atomicfu").id)

        applySettings()
    }

    fun applySettings() = with(project) {
        configureAtomicFUPluginExtension()
    }
}
