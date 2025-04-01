package gradle.plugins.kotlin.apollo.tasks

import com.apollographql.apollo3.gradle.internal.ApolloPushSchemaTask
import gradle.api.provider.tryAssign
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

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
    val projectRootDir: String? = null,
    val revision: String? = null,
    val schema: String? = null,
    val subgraph: String? = null,
) : DefaultTask<ApolloPushSchemaTask>() {

    context(Project)
    override fun applyTo(receiver: ApolloPushSchemaTask) {
        super.applyTo(receiver)

        receiver.graph tryAssign graph
        receiver.graphVariant tryAssign graphVariant
        receiver.key tryAssign key
        receiver::projectRootDir trySet projectRootDir
        receiver.revision tryAssign revision
        receiver.schema tryAssign schema
        receiver.subgraph tryAssign subgraph
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<ApolloPushSchemaTask>())
}

