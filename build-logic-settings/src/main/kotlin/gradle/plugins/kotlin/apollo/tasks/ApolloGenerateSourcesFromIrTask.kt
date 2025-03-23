package gradle.plugins.kotlin.apollo.tasks

import com.apollographql.apollo3.compiler.GeneratedMethod
import com.apollographql.apollo3.compiler.JavaNullable
import com.apollographql.apollo3.compiler.hooks.ApolloCompilerJavaHooks
import com.apollographql.apollo3.compiler.hooks.ApolloCompilerKotlinHooks
import com.apollographql.apollo3.gradle.internal.ApolloGenerateSourcesFromIrTask
import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class ApolloGenerateSourcesFromIrTask(
    override val addJvmOverloads: Boolean? = null,
    override val classesForEnumsMatching: List<String>? = null,
    override val compilerJavaHooks: ApolloCompilerJavaHooks? = null,
    override val compilerKotlinHooks: ApolloCompilerKotlinHooks? = null,
    override val generateFilterNotNull: Boolean? = null,
    override val generateFragmentImplementations: Boolean? = null,
    override val generateInputBuilders: Boolean? = null,
    override val generateMethods: List<GeneratedMethod>? = null,
    override val generateModelBuilders: Boolean? = null,
    override val generateOptionalOperationVariables: Boolean? = null,
    override val generatePrimitiveTypes: Boolean? = null,
    override val generateQueryDocument: Boolean? = null,
    override val generateResponseFields: Boolean? = null,
    override val generateSchema: Boolean? = null,
    override val generatedSchemaName: String? = null,
    override val jsExport: Boolean? = null,
    override val nullableFieldStyle: JavaNullable? = null,
    override val operationManifestFile: String? = null,
    override val operationManifestFormat: String? = null,
    override val outputDir: String? = null,
    override val requiresOptInAnnotation: String? = null,
    override val sealedClassesForEnumsMatching: List<String>? = null,
    override val useSemanticNaming: Boolean? = null,
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
    val codegenSchemas: Set<String>? = null,
    val setCodegenSchemas: Set<String>? = null,
    val irOperations: String? = null,
    val metadataOutputFile: String? = null,
    val upstreamMetadata: Set<String>? = null,
    val setUpstreamMetadata: Set<String>? = null,
    val usedCoordinates: String? = null,
) : ApolloGenerateSourcesBase<ApolloGenerateSourcesFromIrTask>() {

    context(Project)
    override fun applyTo(recipient: ApolloGenerateSourcesFromIrTask) {
        super.applyTo(recipient)

        codegenSchemas?.toTypedArray()?.let(recipient.codegenSchemas::from)
        setCodegenSchemas?.let(recipient.codegenSchemas::setFrom)
        recipient.irOperations tryAssign irOperations?.let(::file)
        recipient.metadataOutputFile tryAssign metadataOutputFile?.let(::file)
        upstreamMetadata?.toTypedArray()?.let(recipient.upstreamMetadata::from)
        setUpstreamMetadata?.let(recipient.upstreamMetadata::setFrom)
        recipient.usedCoordinates tryAssign usedCoordinates?.let(::file)
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<ApolloGenerateSourcesFromIrTask>())
}
