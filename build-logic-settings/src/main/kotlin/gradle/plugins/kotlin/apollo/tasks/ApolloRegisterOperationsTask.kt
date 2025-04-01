package gradle.plugins.kotlin.apollo.tasks

import com.apollographql.apollo3.gradle.internal.ApolloRegisterOperationsTask
import gradle.api.file.tryAssign
import gradle.api.provider.tryAssign
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import klib.data.type.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

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
        receiver.operationOutput tryAssign operationOutput?.let(project::file)
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<ApolloRegisterOperationsTask>())
}
