package gradle.plugins.kotlin.apollo.tasks

import com.apollographql.apollo3.compiler.GeneratedMethod
import com.apollographql.apollo3.compiler.JavaNullable
import com.apollographql.apollo3.compiler.TargetLanguage
import com.apollographql.apollo3.compiler.hooks.ApolloCompilerJavaHooks
import com.apollographql.apollo3.compiler.hooks.ApolloCompilerKotlinHooks
import com.apollographql.apollo3.gradle.internal.ApolloGenerateSourcesFromIrTask
import com.apollographql.apollo3.gradle.internal.ApolloGenerateSourcesTask
import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class ApolloGenerateSourcesTask(
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
    val addTypename: String? = null,
    val alwaysGenerateTypesMatching: Set<String>? = null,
    val setAlwaysGenerateTypesMatching: Set<String>? = null,
    val codegenModels: String? = null,
    val decapitalizeFields: Boolean? = null,
    val failOnWarnings: Boolean? = null,
    val fieldsOnDisjointTypesMustMerge: Boolean? = null,
    val flattenModels: Boolean? = null,
    val generateDataBuilders: Boolean? = null,
    val graphqlFiles: Set<String>? = null,
    val setGraphqlFiles: Set<String>? = null,
    val projectPath: String? = null,
    val scalarAdapterMapping: Map<String, String>? = null,
    val scalarTypeMapping: Map<String, String>? = null,
    val schemaFiles: Set<String>? = null,
    val setSchemaFiles: Set<String>? = null,
    val targetLanguage: TargetLanguage? = null,
    val warnOnDeprecatedUsages: Boolean? = null,
) : ApolloGenerateSourcesBase<ApolloGenerateSourcesTask>() {

    context(Project)
    override fun applyTo(recipient: ApolloGenerateSourcesTask) {
        super.applyTo(recipient)

        recipient.addTypename tryAssign addTypename
        recipient.alwaysGenerateTypesMatching tryAssign alwaysGenerateTypesMatching

        recipient.alwaysGenerateTypesMatching tryAssign setAlwaysGenerateTypesMatching
            ?.let { setAlwaysGenerateTypesMatching ->
                recipient.alwaysGenerateTypesMatching.get() + setAlwaysGenerateTypesMatching
            }

        recipient.codegenModels tryAssign codegenModels
        recipient.decapitalizeFields tryAssign decapitalizeFields
        recipient.failOnWarnings tryAssign failOnWarnings
        recipient.fieldsOnDisjointTypesMustMerge tryAssign fieldsOnDisjointTypesMustMerge
        recipient.flattenModels tryAssign flattenModels
        recipient.generateDataBuilders tryAssign generateDataBuilders
        graphqlFiles?.toTypedArray()?.let(recipient.graphqlFiles ::from)
        setGraphqlFiles?.let(recipient.graphqlFiles ::setFrom)
        recipient.projectPath tryAssign projectPath
        recipient.scalarAdapterMapping tryAssign scalarAdapterMapping
        recipient.scalarTypeMapping tryAssign scalarTypeMapping
        schemaFiles?.toTypedArray()?.let( recipient.schemaFiles ::from)
        setSchemaFiles?.let( recipient.schemaFiles ::setFrom)
        recipient.targetLanguage tryAssign targetLanguage
        recipient.warnOnDeprecatedUsages tryAssign warnOnDeprecatedUsages
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<ApolloGenerateSourcesTask>())
}

