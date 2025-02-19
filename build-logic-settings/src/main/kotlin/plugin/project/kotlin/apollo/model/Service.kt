package plugin.project.kotlin.apollo.model

import com.apollographql.apollo3.compiler.OperationIdGenerator
import com.apollographql.apollo3.compiler.hooks.ApolloCompilerJavaHooks
import kotlinx.serialization.Serializable

@Serializable
internal data class Service(
    val addJvmOverloads: Boolean? = null,

    val addTypename: String? = null,

    val alwaysGenerateTypesMatching: Set<String>? = null,

    val classesForEnumsMatching: List<String>? = null,

    val codegenModels: String? = null,

    val compilerJavaHooks: List<ApolloCompilerJavaHooks>? = null,

//    val compilerKotlinHooks: List<ApolloCompilerKotlinHooks>? = null,

    val debugDir: String? = null,

    val decapitalizeFields: Boolean? = null,

    val excludes: List<String>? = null,

    val failOnWarnings: Boolean? = null,

    val fieldsOnDisjointTypesMustMerge: Boolean? = null,

    val flattenModels: Boolean? = null,

    val generateApolloMetadata: Boolean? = null,

    val generateAsInternal: Boolean? = null,

    val generateDataBuilders: Boolean? = null,

    val generateFragmentImplementations: Boolean? = null,

    val generateInputBuilders: Boolean? = null,

    val generateKotlinModels: Boolean? = null,

    val generateMethods: List<String>? = null,

    val generateModelBuilders: Boolean? = null,

    val generateOperationOutput: Boolean? = null,

    val generateOptionalOperationVariables: Boolean? = null,

    val generatePrimitiveTypes: Boolean? = null,

    val generateQueryDocument: Boolean? = null,

    val generateSchema: Boolean? = null,

    val generatedSchemaName: String? = null,

    val includes: List<String>? = null,

    val jsExport: Boolean? = null,

    val languageVersion: String? = null,

//    val name: String TODO

    val nullableFieldStyle: String? = null,

//    val operationIdGenerator: OperationIdGenerator? = null,

    val operationManifest: String? = null,

    val operationManifestFormat: String? = null,

    val operationOutputFile: String? = null,

//    val operationOutputGenerator: OperationOutputGenerator? = null,

    val outputDir: String? = null,

    val packageName: String? = null,

//    val packageNameGenerator: PackageNameGenerator? = null,

    val requiresOptInAnnotation: String? = null,

    val schemaFile: String? = null,

    val schemaFiles: List<String>? = null,

    val sealedClassesForEnumsMatching: List<String>? = null,

    val sourceFolder: String? = null,

    val testDir: String? = null,

    val useSemanticNaming: Boolean? = null,

    val warnOnDeprecatedUsages: Boolean? = null,

//    fun dependsOn(dependencyNotation: kotlin.Any): kotlin.Unit
//
//    fun introspection(configure: org.gradle.api.Action<in com.apollographql.apollo3.gradle.api.Introspection>): kotlin.Unit
//
//    fun isADependencyOf(dependencyNotation: kotlin.Any): kotlin.Unit
//
//    fun mapScalar(graphQLName: kotlin.String, targetName: kotlin.String): kotlin.Unit
//
//    fun mapScalar(graphQLName: kotlin.String, targetName: kotlin.String, expression: kotlin.String): kotlin.Unit
//
//    fun mapScalarToJavaBoolean(graphQLName: kotlin.String): kotlin.Unit
//
//    fun mapScalarToJavaDouble(graphQLName: kotlin.String): kotlin.Unit
//
//    fun mapScalarToJavaFloat(graphQLName: kotlin.String): kotlin.Unit
//
//    fun mapScalarToJavaInteger(graphQLName: kotlin.String): kotlin.Unit
//
//    fun mapScalarToJavaLong(graphQLName: kotlin.String): kotlin.Unit
//
//    fun mapScalarToJavaObject(graphQLName: kotlin.String): kotlin.Unit
//
//    fun mapScalarToJavaString(graphQLName: kotlin.String): kotlin.Unit
//
//    fun mapScalarToKotlinAny(graphQLName: kotlin.String): kotlin.Unit
//
//    fun mapScalarToKotlinBoolean(graphQLName: kotlin.String): kotlin.Unit
//
//    fun mapScalarToKotlinDouble(graphQLName: kotlin.String): kotlin.Unit
//
//    fun mapScalarToKotlinFloat(graphQLName: kotlin.String): kotlin.Unit
//
//    fun mapScalarToKotlinInt(graphQLName: kotlin.String): kotlin.Unit
//
//    fun mapScalarToKotlinLong(graphQLName: kotlin.String): kotlin.Unit
//
//    fun mapScalarToKotlinString(graphQLName: kotlin.String): kotlin.Unit
//
//    fun mapScalarToUpload(graphQLName: kotlin.String): kotlin.Unit
//
//    fun operationManifestConnection(action: org.gradle.api.Action<in com.apollographql.apollo3.gradle.api.Service.OperationManifestConnection>): kotlin.Unit
//
//    fun operationOutputConnection(action: org.gradle.api.Action<in com.apollographql.apollo3.gradle.api.Service.OperationOutputConnection>): kotlin.Unit
//
//    fun outputDirConnection(action: org.gradle.api.Action<in com.apollographql.apollo3.gradle.api.Service.DirectoryConnection>): kotlin.Unit
//
//    fun packageNamesFromFilePaths(rootPackageName: kotlin.String? = COMPILED_CODE): kotlin.Unit
//
//    fun registerOperations(configure: org.gradle.api.Action<in com.apollographql.apollo3.gradle.api.RegisterOperationsConfig>): kotlin.Unit
//
//    fun registry(configure: org.gradle.api.Action<in com.apollographql.apollo3.gradle.api.Registry>): kotlin.Unit
//
//    fun srcDir(directory: kotlin.Any): kotlin.Unit
//
//    fun usedCoordinates(file: java.io.File): kotlin.Unit
//
//    fun usedCoordinates(file: kotlin.String): kotlin.Unit
//
//    public interface DirectoryConnection {
//        val outputDir: Provider<Directory>
//
//        val task: org.gradle.api.tasks.TaskProvider<out org.gradle.api.Task>
//
//        fun connectToAllAndroidVariants(): kotlin.Unit
//
//        fun connectToAndroidSourceSet(name: kotlin.String): kotlin.Unit
//
//        fun connectToAndroidVariant(variant: kotlin.Any): kotlin.Unit
//
//        fun connectToJavaSourceSet(name: kotlin.String): kotlin.Unit
//
//        fun connectToKotlinSourceSet(name: kotlin.String): kotlin.Unit
//    }
//
//    public final class OperationManifestConnection public constructor(task: org.gradle.api.tasks.TaskProvider<out org.gradle.api.Task>, manifest: Provider<RegularFile>) {
//        public final val manifest: Provider<RegularFile> /* compiled code */
//
//        public final val task: org.gradle.api.tasks.TaskProvider<out org.gradle.api.Task> /* compiled code */
//    }
//
//    public final class OperationOutputConnection public constructor(task: org.gradle.api.tasks.TaskProvider<out org.gradle.api.Task>, operationOutputFile: Provider<RegularFile>) {
//        public final val operationOutputFile: Provider<RegularFile> /* compiled code */
//
//        public final val task: org.gradle.api.tasks.TaskProvider<out org.gradle.api.Task> /* compiled code */
//    }
)

