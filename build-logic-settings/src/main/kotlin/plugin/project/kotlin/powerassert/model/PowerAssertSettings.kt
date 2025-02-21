package plugin.project.kotlin.powerassert.model

import gradle.tryAssign
import kotlinx.serialization.Serializable

@Serializable
internal data class PowerAssertSettings(
    override val functions: Set<String>? = null,
    override val includedSourceSets: Set<String>? = null,
    val enabled: Boolean = true
) : PowerAssertGradleExtension {

    fun applyTo(extension: org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension) {
        extension.functions tryAssign functions
        extension.includedSourceSets tryAssign includedSourceSets
    }
}
