package plugin.project.kotlin.atomicfu.model

import gradle.id
import gradle.libs
import gradle.model.kotlin.atomicfu.AtomicFUExtension
import gradle.model.project.EnabledSettings
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class AtomicFUSettings(
    override val dependenciesVersion: String? = null,
    override val transformJvm: Boolean? = null,
    override val jvmVariant: String? = null,
    override val verbose: Boolean? = null,
    override val enabled: Boolean = true
) : AtomicFUExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("atomicfu").id) {
            super.applyTo()
        }
}
