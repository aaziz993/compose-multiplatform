package gradle.plugins.kotlin.apollo

import com.apollographql.apollo3.compiler.PackageNameGenerator
import com.apollographql.apollo3.compiler.hooks.ApolloCompilerJavaHooks
import com.apollographql.apollo3.gradle.api.Service
import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class Service(
    val addJvmOverloads: Boolean? = null,
    val addTypename: String? = null,
    val alwaysGenerateTypesMatching: Set<String>? = null,
    val classesForEnumsMatching: List<String>? = null,
    val codegenModels: String? = null,
    val compilerJavaHooks: List<ApolloCompilerJavaHooks>? = null,
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
    val name: String,
    val nullableFieldStyle: String? = null,
    val operationManifest: String? = null,
    val operationManifestFormat: String? = null,
    val operationOutputFile: String? = null,
    val outputDir: String? = null,
    val packageName: String? = null,
    val packageNameGenerator: PackageNameGenerator? = null,
    val requiresOptInAnnotation: String? = null,
    val schemaFile: String? = null,
    val schemaFiles: List<String>? = null,
    val sealedClassesForEnumsMatching: List<String>? = null,
    val sourceFolder: String? = null,
    val testDir: String? = null,
    val useSemanticNaming: Boolean? = null,
    val warnOnDeprecatedUsages: Boolean? = null,
) {

    context(Project)
    fun applyTo(service: Service) {
        service.addJvmOverloads tryAssign addJvmOverloads
        service.addTypename tryAssign addTypename
        service.alwaysGenerateTypesMatching tryAssign alwaysGenerateTypesMatching
        service.classesForEnumsMatching tryAssign classesForEnumsMatching
        service.codegenModels tryAssign codegenModels
        service.compilerJavaHooks tryAssign compilerJavaHooks
        service.debugDir tryAssign debugDir?.let(layout.projectDirectory::dir)
        service.decapitalizeFields tryAssign decapitalizeFields
        service.excludes tryAssign excludes
        service.failOnWarnings tryAssign failOnWarnings
        service.fieldsOnDisjointTypesMustMerge tryAssign fieldsOnDisjointTypesMustMerge
        service.flattenModels tryAssign flattenModels
        service.generateApolloMetadata tryAssign generateApolloMetadata
        service.generateAsInternal tryAssign generateAsInternal
        service.generateDataBuilders tryAssign generateDataBuilders
        service.generateFragmentImplementations tryAssign generateFragmentImplementations
        service.generateInputBuilders tryAssign generateInputBuilders
        service.generateKotlinModels tryAssign generateKotlinModels
        service.generateMethods tryAssign generateMethods
        service.generateModelBuilders tryAssign generateModelBuilders
        service.generateOperationOutput tryAssign generateOperationOutput
        service.generateOptionalOperationVariables tryAssign generateOptionalOperationVariables
        service.generatePrimitiveTypes tryAssign generatePrimitiveTypes
        service.generateQueryDocument tryAssign generateQueryDocument
        service.generateSchema tryAssign generateSchema
        service.generatedSchemaName tryAssign generatedSchemaName
        service.includes tryAssign includes
        service.jsExport tryAssign jsExport
        service.languageVersion tryAssign languageVersion
        service.nullableFieldStyle tryAssign nullableFieldStyle
        service.operationManifest tryAssign operationManifest?.let(::file)
        service.operationManifestFormat tryAssign operationManifestFormat
        service.operationOutputFile tryAssign operationOutputFile?.let(::file)
        service.outputDir tryAssign outputDir?.let(layout.projectDirectory::dir)
        service.packageName tryAssign packageName
        service.requiresOptInAnnotation tryAssign requiresOptInAnnotation
        service.schemaFile tryAssign schemaFile?.let(::file)
        schemaFiles?.let(service.schemaFiles::setFrom)
        service.sealedClassesForEnumsMatching tryAssign sealedClassesForEnumsMatching
        service.sourceFolder tryAssign sourceFolder
        service.testDir tryAssign testDir?.let(layout.projectDirectory::dir)
        service.useSemanticNaming tryAssign useSemanticNaming
        service.warnOnDeprecatedUsages tryAssign warnOnDeprecatedUsages
    }
}
