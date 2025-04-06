package gradle.plugins.kotlin.apollo.tasks

import com.apollographql.apollo3.gradle.internal.ApolloDownloadSchemaTask
import gradle.api.file.tryAssign
import gradle.api.provider.tryAssign
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import klib.data.type.serialization.json.serializer.SerializableAnyMap
import klib.data.type.reflection.tryPlus
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class ApolloDownloadSchemaTask(
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
    val endpoint: String? = null,
    val graph: String? = null,
    val graphVariant: String? = null,
    val header: List<String>? = null,
    val setHeader: List<String>? = null,
    val insecure: Boolean? = null,
    val key: String? = null,
    val outputFile: String? = null,
    val projectRootDir: String? = null,
    val registryUrl: String? = null,
    val schema: String? = null,
) : DefaultTask<ApolloDownloadSchemaTask>() {

    context(Project)
    override fun applyTo(receiver: ApolloDownloadSchemaTask) {
        super.applyTo(receiver)

        receiver.endpoint tryAssign endpoint
        receiver.graph tryAssign graph
        receiver.graphVariant tryAssign graphVariant
        receiver::header tryPlus header
        receiver::header trySet setHeader
        receiver.insecure tryAssign insecure
        receiver.key tryAssign key
        receiver.outputFile tryAssign outputFile?.let(project::file)
        receiver::projectRootDir trySet projectRootDir
        receiver.registryUrl tryAssign registryUrl
        receiver.schema tryAssign schema
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<ApolloDownloadSchemaTask>())
}
