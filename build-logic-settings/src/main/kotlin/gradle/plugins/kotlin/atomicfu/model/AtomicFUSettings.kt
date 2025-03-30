package gradle.plugins.kotlin.atomicfu.model

import gradle.accessors.catalog.libs

import gradle.accessors.settings
import gradle.plugins.kotlin.atomicfu.AtomicFUExtension
import gradle.api.EnabledSettings
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
        project.pluginManager.withPlugin(project.settings.libs.plugin("atomicfu").id) {
            super.applyTo()
        }
}
