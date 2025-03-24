package gradle.plugins.kotlin.apollo.tasks

import com.apollographql.apollo3.compiler.TargetLanguage
import com.apollographql.apollo3.gradle.internal.ApolloGenerateSchemaTask
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class ApolloGenerateSchemaTask(
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
    val codegenModels: String? = null,
    val generateDataBuilders: Boolean? = null,
    val outputFile: String? = null,
    val scalarAdapterMapping: Map<String, String>? = null,
    val setScalarAdapterMapping: Map<String, String>? = null,
    val scalarTypeMapping: Map<String, String>? = null,
    val setScalarTypeMapping: Map<String, String>? = null,
    val schemaFiles: Set<String>? = null,
    val setSchemaFiles: Set<String>? = null,
    val targetLanguage: TargetLanguage? = null,
    val upstreamSchemaFiles: Set<String>? = null,
    val setUpstreamSchemaFiles: Set<String>? = null,
    val userCodegenModels: String? = null,
    val userGenerateKotlinModels: Boolean? = null,
) : DefaultTask<ApolloGenerateSchemaTask>() {

    context(project: Project)
    override fun applyTo(receiver: ApolloGenerateSchemaTask) {
        super.applyTo(receiver)

        receiver.codegenModels tryAssign codegenModels
        receiver.generateDataBuilders tryAssign generateDataBuilders
        receiver.outputFile tryAssign outputFile?.let(project::file)

        receiver.scalarAdapterMapping tryAssign scalarAdapterMapping?.let { scalarAdapterMapping ->
            receiver.scalarAdapterMapping.get() + scalarAdapterMapping
        }

        receiver.scalarAdapterMapping tryAssign setScalarAdapterMapping

        receiver.scalarTypeMapping tryAssign scalarTypeMapping?.let { scalarTypeMapping ->
            receiver.scalarTypeMapping.get() + scalarTypeMapping
        }

        receiver.scalarTypeMapping tryAssign setScalarAdapterMapping
        schemaFiles?.toTypedArray()?.let(receiver.schemaFiles::from)
        setSchemaFiles?.toTypedArray()?.let(receiver.schemaFiles::setFrom)
        receiver.targetLanguage tryAssign targetLanguage
        upstreamSchemaFiles?.toTypedArray()?.let(receiver.upstreamSchemaFiles::from)
        setUpstreamSchemaFiles?.toTypedArray()?.let(receiver.upstreamSchemaFiles::setFrom)
        receiver.userCodegenModels tryAssign userCodegenModels
        receiver.userGenerateKotlinModels tryAssign userGenerateKotlinModels
    }

    context(project: Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<ApolloGenerateSchemaTask>())
}
