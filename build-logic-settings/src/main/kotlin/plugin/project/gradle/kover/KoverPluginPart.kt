package plugin.project.gradle.kover

import gradle.amperModuleExtraProperties
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx

internal class KoverPluginPart(ctx: PluginPartCtx) : BindingPluginPart by ctx {

    override val needToApply: Boolean by lazy {
       project.amperModuleExtraProperties.settings.gradle.kover.enabled
    }

    override fun applyBeforeEvaluate() {
        project.plugins.apply(project.settings.libs.plugins.plugin("kover").id)

        applySettings()
    }

    private fun applySettings() = with(project) {
        configureKoverExtension()
    }
}
