package plugins.kotlin.powerassert.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.kotlin.powerassert.PowerAssertGradleExtension
import gradle.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

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
