package plugin.project.kotlin.powerassert.model

import gradle.id
import gradle.libs
import gradle.model.kotlin.powerassert.PowerAssertGradleExtension
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import gradle.model.project.EnabledSettings

@Serializable
internal data class PowerAssertSettings(
    override val functions: Set<String>? = null,
    override val includedSourceSets: Set<String>? = null,
    override val enabled: Boolean = true
) : PowerAssertGradleExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("power.assert").id) {
            super.applyTo()
        }
}
