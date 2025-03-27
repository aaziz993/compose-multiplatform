package gradle.plugins.kotlin.apollo.tasks

import com.apollographql.apollo3.compiler.GeneratedMethod
import com.apollographql.apollo3.compiler.JavaNullable
import com.apollographql.apollo3.compiler.hooks.ApolloCompilerJavaHooks
import com.apollographql.apollo3.compiler.hooks.ApolloCompilerKotlinHooks
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import gradle.api.tryAddAll
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class ApolloGenerateSourcesBase<T : com.apollographql.apollo3.gradle.internal.ApolloGenerateSourcesBase> : DefaultTask<T>() {

    abstract val addJvmOverloads: Boolean?

    abstract val classesForEnumsMatching: List<String>?

    abstract val setClassesForEnumsMatching: List<String>?

    abstract val compilerJavaHooks: ApolloCompilerJavaHooks?

    abstract val compilerKotlinHooks: ApolloCompilerKotlinHooks?

    abstract val generateFilterNotNull: Boolean?

    abstract val generateFragmentImplementations: Boolean?

    abstract val generateInputBuilders: Boolean?

    abstract val generateMethods: List<GeneratedMethod>?
    abstract val setGenerateMethods: List<GeneratedMethod>?

    abstract val generateModelBuilders: Boolean?

    abstract val generateOptionalOperationVariables: Boolean?

    abstract val generatePrimitiveTypes: Boolean?

    abstract val generateQueryDocument: Boolean?

    abstract val generateResponseFields: Boolean?

    abstract val generateSchema: Boolean?

    abstract val generatedSchemaName: String?

    abstract val jsExport: Boolean?

    abstract val nullableFieldStyle: JavaNullable?

    abstract val operationManifestFile: String?

    abstract val operationManifestFormat: String?

    abstract val outputDir: String?

    abstract val requiresOptInAnnotation: String?

    abstract val sealedClassesForEnumsMatching: List<String>?

    abstract val setSealedClassesForEnumsMatching: List<String>?

    abstract val useSemanticNaming: Boolean?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver.addJvmOverloads tryAssign addJvmOverloads
        receiver.classesForEnumsMatching tryAddAll classesForEnumsMatching
        receiver.classesForEnumsMatching tryAssign setClassesForEnumsMatching
        receiver.generateFilterNotNull tryAssign generateFilterNotNull
        receiver.generateFragmentImplementations tryAssign generateFragmentImplementations
        receiver.generateInputBuilders tryAssign generateInputBuilders
        receiver.generateMethods tryAddAll generateMethods
        receiver.generateMethods tryAssign setGenerateMethods
        receiver.generateModelBuilders tryAssign generateModelBuilders
        receiver.generateOptionalOperationVariables tryAssign generateOptionalOperationVariables
        receiver.generatePrimitiveTypes tryAssign generatePrimitiveTypes
        receiver.generateQueryDocument tryAssign generateQueryDocument
        receiver.generateResponseFields tryAssign generateResponseFields
        receiver.generateSchema tryAssign generateSchema
        receiver.generatedSchemaName tryAssign generatedSchemaName
        receiver.jsExport tryAssign jsExport
        receiver.nullableFieldStyle tryAssign nullableFieldStyle
        receiver.operationManifestFile tryAssign operationManifestFile?.let(project::file)
        receiver.operationManifestFormat tryAssign operationManifestFormat
        receiver.outputDir tryAssign outputDir?.let(project.layout.projectDirectory::dir)
        receiver.requiresOptInAnnotation tryAssign requiresOptInAnnotation
        receiver.sealedClassesForEnumsMatching tryAddAll sealedClassesForEnumsMatching
        receiver.sealedClassesForEnumsMatching tryAssign setSealedClassesForEnumsMatching
        receiver.useSemanticNaming tryAssign useSemanticNaming
    }
}

@Serializable
@SerialName("ApolloGenerateSourcesBase")
internal data class ApolloGenerateSourcesBaseImpl(
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
) : ApolloGenerateSourcesBase<com.apollographql.apollo3.gradle.internal.ApolloGenerateSourcesBase>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<com.apollographql.apollo3.gradle.internal.ApolloGenerateSourcesBase>())
}
