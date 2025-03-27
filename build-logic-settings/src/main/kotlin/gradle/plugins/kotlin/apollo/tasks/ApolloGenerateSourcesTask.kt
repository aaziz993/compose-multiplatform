package gradle.plugins.kotlin.apollo.tasks

import com.apollographql.apollo3.compiler.GeneratedMethod
import com.apollographql.apollo3.compiler.JavaNullable
import com.apollographql.apollo3.compiler.TargetLanguage
import com.apollographql.apollo3.compiler.hooks.ApolloCompilerJavaHooks
import com.apollographql.apollo3.compiler.hooks.ApolloCompilerKotlinHooks
import com.apollographql.apollo3.gradle.internal.ApolloGenerateSourcesTask
import gradle.api.tasks.applyTo
import gradle.api.tryAddAll
import gradle.api.tryAssign
import gradle.api.tryPutAll
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class ApolloGenerateSourcesTask(
    override val addJvmOverloads: Boolean? = null,
    override val classesForEnumsMatching: List<String>? = null,
    override val setClassesForEnumsMatching: List<String>? = null,
    override val compilerJavaHooks: ApolloCompilerJavaHooks? = null,
    override val compilerKotlinHooks: ApolloCompilerKotlinHooks? = null,
    override val generateFilterNotNull: Boolean? = null,
    override val generateFragmentImplementations: Boolean? = null,
    override val generateInputBuilders: Boolean? = null,
    override val generateMethods: List<GeneratedMethod>? = null,
    override val setGenerateMethods: List<GeneratedMethod>? = null,
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
    override val setSealedClassesForEnumsMatching: List<String>? = null,
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
    val setScalarAdapterMapping: Map<String, String>? = null,
    val scalarTypeMapping: Map<String, String>? = null,
    val setScalarTypeMapping: Map<String, String>? = null,
    val schemaFiles: Set<String>? = null,
    val setSchemaFiles: Set<String>? = null,
    val targetLanguage: TargetLanguage? = null,
    val warnOnDeprecatedUsages: Boolean? = null,
) : ApolloGenerateSourcesBase<ApolloGenerateSourcesTask>() {

    context(Project)
    override fun applyTo(receiver: ApolloGenerateSourcesTask) {
        super.applyTo(receiver)

        receiver.addTypename tryAssign addTypename
        receiver.alwaysGenerateTypesMatching tryAddAll  alwaysGenerateTypesMatching
        receiver.alwaysGenerateTypesMatching tryAssign setAlwaysGenerateTypesMatching
        receiver.codegenModels tryAssign codegenModels
        receiver.decapitalizeFields tryAssign decapitalizeFields
        receiver.failOnWarnings tryAssign failOnWarnings
        receiver.fieldsOnDisjointTypesMustMerge tryAssign fieldsOnDisjointTypesMustMerge
        receiver.flattenModels tryAssign flattenModels
        receiver.generateDataBuilders tryAssign generateDataBuilders
        receiver.graphqlFiles tryFrom graphqlFiles
        receiver.graphqlFiles trySetFrom setGraphqlFiles
        receiver.projectPath tryAssign projectPath
        receiver.scalarAdapterMapping tryPutAll  scalarAdapterMapping
        receiver.scalarAdapterMapping tryAssign setScalarAdapterMapping
        receiver.scalarTypeMapping tryPutAll  scalarTypeMapping
        receiver.scalarTypeMapping tryAssign setScalarAdapterMapping
        receiver.schemaFiles tryFrom schemaFiles
        receiver.schemaFiles trySetFrom setSchemaFiles
        receiver.targetLanguage tryAssign targetLanguage
        receiver.warnOnDeprecatedUsages tryAssign warnOnDeprecatedUsages
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<ApolloGenerateSourcesTask>())
}

