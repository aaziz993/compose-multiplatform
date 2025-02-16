package plugin.project.gradle.spotless

import com.diffplug.gradle.spotless.SpotlessExtension
import gradle.amperModuleExtraProperties
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx

internal class SpotlessPluginPart(ctx: PluginPartCtx) : BindingPluginPart by ctx {

    private val spotless by lazy {
        project.amperModuleExtraProperties.settings.gradle.spotless
    }

    override val needToApply: Boolean by lazy {
        spotless.enabled
    }

    override fun applyBeforeEvaluate() = with(project) {
        super.applyAfterEvaluate()

        plugins.apply(project.settings.libs.plugins.plugin("spotless").id)

        applySettings()
    }

    fun applySettings() = with(project) {
        extensions.configure<SpotlessExtension>(::configureSpotlessExtension)
    }
}
