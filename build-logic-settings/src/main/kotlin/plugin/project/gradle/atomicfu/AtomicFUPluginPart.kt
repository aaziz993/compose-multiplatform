package plugin.project.gradle.atomicfu

import org.gradle.kotlin.dsl.configure
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx
import gradle.amperModuleExtraProperties
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.atomicfu.plugin.gradle.AtomicFUPluginExtension
import org.gradle.kotlin.dsl.apply

internal class AtomicFUPluginPart(ctx: PluginPartCtx) : BindingPluginPart by ctx {

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
