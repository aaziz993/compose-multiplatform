package gradle.plugins.kotlin.apollo.tasks

import org.gradle.kotlin.dsl.withType
import com.apollographql.apollo3.gradle.internal.ApolloPushSchemaTask
import kotlinx.serialization.Serializable
import gradle.api.tasks.DefaultTask
import org.gradle.api.Project
import gradle.collection.SerializableAnyMap
import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.api.trySet

@Serializable
internal data class ApolloPushSchemaTask(
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
    override val name: String? = null,
    val graph: String? = null,
    val graphVariant: String? = null,
    val key: String? = null,
    var projectRootDir: String? = null,
    val revision: String? = null,
    val schema: String? = null,
    val subgraph: String? = null,
) : DefaultTask<ApolloPushSchemaTask>() {

    context(Project)
    override fun applyTo(recipient: ApolloPushSchemaTask) {
        super.applyTo(recipient)

        recipient.graph tryAssign graph
        recipient.graphVariant tryAssign graphVariant
        recipient.key tryAssign key
        recipient::projectRootDir trySet projectRootDir
        recipient.revision tryAssign revision
        recipient.schema tryAssign schema
        recipient.subgraph tryAssign subgraph
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<ApolloPushSchemaTask>())
}

