package gradle.plugins.kotlin.apollo.tasks

import arrow.core.raise.recover
import org.gradle.kotlin.dsl.withType
import com.apollographql.apollo3.gradle.internal.ApolloDownloadSchemaTask
import kotlinx.serialization.Serializable
import gradle.api.tasks.DefaultTask
import org.gradle.api.Project
import gradle.collection.SerializableAnyMap
import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.api.trySet

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
        receiver::header trySet header?.let { header -> receiver.header + header }
        receiver::header trySet setHeader
        receiver.insecure tryAssign insecure
        receiver.key tryAssign key
        receiver.outputFile tryAssign outputFile?.let(::file)
        receiver::projectRootDir trySet projectRootDir
        receiver.registryUrl tryAssign registryUrl
        receiver.schema tryAssign schema
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<ApolloDownloadSchemaTask>())
}
