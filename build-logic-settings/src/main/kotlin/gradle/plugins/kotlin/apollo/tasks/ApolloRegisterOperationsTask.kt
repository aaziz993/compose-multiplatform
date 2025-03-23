package gradle.plugins.kotlin.apollo.tasks

import org.gradle.kotlin.dsl.withType
import com.apollographql.apollo3.gradle.internal.ApolloRegisterOperationsTask
import kotlinx.serialization.Serializable
import gradle.api.tasks.DefaultTask
import org.gradle.api.Project
import gradle.collection.SerializableAnyMap
import gradle.api.tasks.applyTo
import gradle.api.tryAssign

@Serializable
internal data class ApolloRegisterOperationsTask(
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
    val listId: String? = null,
    val operationManifestFormat: String? = null,
    val operationOutput: String? = null,
) : DefaultTask<ApolloRegisterOperationsTask>() {

    context(Project)
    override fun applyTo(receiver: ApolloRegisterOperationsTask) {
        super.applyTo(receiver)

        receiver.graph tryAssign graph
        receiver.graphVariant tryAssign graphVariant
        receiver.key tryAssign key
        receiver.listId tryAssign listId
        receiver.operationManifestFormat tryAssign operationManifestFormat
        receiver.operationOutput tryAssign operationOutput?.let(::file)
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<ApolloRegisterOperationsTask>())
}
