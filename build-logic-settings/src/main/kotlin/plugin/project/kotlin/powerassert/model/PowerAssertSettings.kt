package plugin.project.kotlin.powerassert.model

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.powerAssert
import gradle.settings
import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.model.EnabledSettings

@Serializable
internal data class PowerAssertSettings(
    override val functions: Set<String>? = null,
    override val includedSourceSets: Set<String>? = null,
    override val enabled: Boolean = true
) : PowerAssertGradleExtension, EnabledSettings {

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("power.assert").id) {
            powerAssert.functions tryAssign functions
            powerAssert.includedSourceSets tryAssign includedSourceSets
        }
}
