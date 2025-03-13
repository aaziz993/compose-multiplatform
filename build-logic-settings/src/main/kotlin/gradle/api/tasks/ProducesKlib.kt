@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.api.tasks

import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project

internal interface ProducesKlib : Task {

    val produceUnpackagedKlib: Boolean?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as org.jetbrains.kotlin.gradle.internal.tasks.ProducesKlib

        named.produceUnpackagedKlib tryAssign produceUnpackagedKlib
    }
}

@Serializable
@SerialName("ProducesKlib")
internal data class ProducesKlibImpl(
    override val produceUnpackagedKlib: Boolean? = null,
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    override val name: String = ""
) : ProducesKlib
