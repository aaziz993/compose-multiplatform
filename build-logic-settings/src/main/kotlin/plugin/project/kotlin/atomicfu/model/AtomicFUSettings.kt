package plugin.project.kotlin.atomicfu.model

import gradle.atomicFU
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.model.EnabledSettings

@Serializable
internal data class AtomicFUSettings(
    override val dependenciesVersion: String? = null,
    override val transformJvm: Boolean? = null,
    override val jvmVariant: String? = null,
    override val verbose: Boolean? = null,
    override val enabled: Boolean = true
) : AtomicFUExtension, EnabledSettings {

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("atomicfu").id) {
            atomicFU::dependenciesVersion trySet dependenciesVersion
            atomicFU::transformJvm trySet transformJvm
            atomicFU::jvmVariant trySet jvmVariant
            atomicFU::verbose trySet verbose
        }
}
