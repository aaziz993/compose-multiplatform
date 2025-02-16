package plugin.project.gradle.spotless

import com.diffplug.gradle.spotless.SpotlessExtension
import gradle.amperModuleExtraProperties
import gradle.libs
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

    override fun applyAfterEvaluate() = with(project) {
        super.applyAfterEvaluate()
        applySettings()
    }

    fun applySettings() = with(project) {
        plugins.apply(libs.plugins.spotless.get().pluginId)

        apply(plugin = libs.plugins.spotless.get().pluginId)

        extensions.configure<SpotlessExtension>(::configureSpotlessExtension)
    }
}
