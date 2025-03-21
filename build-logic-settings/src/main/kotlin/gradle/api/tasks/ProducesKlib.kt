@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.api.tasks

import org.gradle.kotlin.dsl.withType
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal interface ProducesKlib<T : org.jetbrains.kotlin.gradle.internal.tasks.ProducesKlib> : Task<T> {

    val produceUnpackagedKlib: Boolean?

    context(Project)
    override fun applyTo(recipient: T) {
        super.applyTo(recipient)

        recipient.produceUnpackagedKlib tryAssign produceUnpackagedKlib
    }
}

@Serializable
@SerialName("ProducesKlib")
internal data class ProducesKlibImpl(
    override val produceUnpackagedKlib: Boolean? = null,
    override val dependsOn: LinkedHashSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: LinkedHashSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String = ""
) : ProducesKlib<org.jetbrains.kotlin.gradle.internal.tasks.ProducesKlib>{

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<org.jetbrains.kotlin.gradle.internal.tasks.ProducesKlib>())
}
