package gradle.plugins.kotlin.apollo.tasks

import com.apollographql.apollo3.compiler.TargetLanguage
import org.gradle.kotlin.dsl.withType
import com.apollographql.apollo3.gradle.internal.ApolloGenerateSchemaTask
import kotlinx.serialization.Serializable
import gradle.api.tasks.DefaultTask
import org.gradle.api.Project
import gradle.collection.SerializableAnyMap
import gradle.api.tasks.applyTo
import gradle.api.tryAssign

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

    context(Project)
    override fun applyTo(recipient: ApolloGenerateSchemaTask) {
        super.applyTo(recipient)

        recipient.codegenModels tryAssign codegenModels
        recipient.generateDataBuilders tryAssign generateDataBuilders
        recipient.outputFile tryAssign outputFile?.let(::file)
        recipient.scalarAdapterMapping tryAssign scalarAdapterMapping

        recipient.scalarAdapterMapping tryAssign setScalarAdapterMapping?.let { setScalarAdapterMapping ->
            recipient.scalarAdapterMapping.get() + setScalarAdapterMapping
        }

        recipient.scalarTypeMapping tryAssign scalarTypeMapping

        recipient.scalarTypeMapping tryAssign setScalarTypeMapping?.let { setScalarTypeMapping ->
            recipient.scalarTypeMapping.get() + setScalarTypeMapping
        }

        schemaFiles?.toTypedArray()?.let(recipient.schemaFiles::from)
        setSchemaFiles?.toTypedArray()?.let(recipient.schemaFiles::setFrom)
        recipient.targetLanguage tryAssign targetLanguage
        upstreamSchemaFiles?.toTypedArray()?.let(recipient.upstreamSchemaFiles::from)
        setUpstreamSchemaFiles?.toTypedArray()?.let(recipient.upstreamSchemaFiles::setFrom)
        recipient.userCodegenModels tryAssign userCodegenModels
        recipient.userGenerateKotlinModels tryAssign userGenerateKotlinModels
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<ApolloGenerateSchemaTask>())
}
