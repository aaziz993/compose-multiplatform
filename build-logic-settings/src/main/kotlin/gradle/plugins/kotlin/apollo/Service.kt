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
    val classesForEnumsMatching: Set<String>? = null,
    val codegenModels: String? = null,
    val compilerJavaHooks: Set<ApolloCompilerJavaHooks>? = null,
    val debugDir: String? = null,
    val decapitalizeFields: Boolean? = null,
    val excludes: Set<String>? = null,
    val failOnWarnings: Boolean? = null,
    val fieldsOnDisjointTypesMustMerge: Boolean? = null,
    val flattenModels: Boolean? = null,
    val generateApolloMetadata: Boolean? = null,
    val generateAsInternal: Boolean? = null,
    val generateDataBuilders: Boolean? = null,
    val generateFragmentImplementations: Boolean? = null,
    val generateInputBuilders: Boolean? = null,
    val generateKotlinModels: Boolean? = null,
    val generateMethods: Set<String>? = null,
    val generateModelBuilders: Boolean? = null,
    val generateOperationOutput: Boolean? = null,
    val generateOptionalOperationVariables: Boolean? = null,
    val generatePrimitiveTypes: Boolean? = null,
    val generateQueryDocument: Boolean? = null,
    val generateSchema: Boolean? = null,
    val generatedSchemaName: String? = null,
    val includes: Set<String>? = null,
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
    val schemaFiles: Set<String>? = null,
    val setSchemaFiles: Set<String>? = null,
    val sealedClassesForEnumsMatching: Set<String>? = null,
    val sourceFolder: String? = null,
    val testDir: String? = null,
    val useSemanticNaming: Boolean? = null,
    val warnOnDeprecatedUsages: Boolean? = null,
) {

    context(Project)
    fun applyTo(recipient: Service) {
        recipient.addJvmOverloads tryAssign addJvmOverloads
        recipient.addTypename tryAssign addTypename
        recipient.alwaysGenerateTypesMatching tryAssign alwaysGenerateTypesMatching
        recipient.classesForEnumsMatching tryAssign classesForEnumsMatching
        recipient.codegenModels tryAssign codegenModels
        recipient.compilerJavaHooks tryAssign compilerJavaHooks
        recipient.debugDir tryAssign debugDir?.let(layout.projectDirectory::dir)
        recipient.decapitalizeFields tryAssign decapitalizeFields
        recipient.excludes tryAssign excludes
        recipient.failOnWarnings tryAssign failOnWarnings
        recipient.fieldsOnDisjointTypesMustMerge tryAssign fieldsOnDisjointTypesMustMerge
        recipient.flattenModels tryAssign flattenModels
        recipient.generateApolloMetadata tryAssign generateApolloMetadata
        recipient.generateAsInternal tryAssign generateAsInternal
        recipient.generateDataBuilders tryAssign generateDataBuilders
        recipient.generateFragmentImplementations tryAssign generateFragmentImplementations
        recipient.generateInputBuilders tryAssign generateInputBuilders
        recipient.generateKotlinModels tryAssign generateKotlinModels
        recipient.generateMethods tryAssign generateMethods
        recipient.generateModelBuilders tryAssign generateModelBuilders
        recipient.generateOperationOutput tryAssign generateOperationOutput
        recipient.generateOptionalOperationVariables tryAssign generateOptionalOperationVariables
        recipient.generatePrimitiveTypes tryAssign generatePrimitiveTypes
        recipient.generateQueryDocument tryAssign generateQueryDocument
        recipient.generateSchema tryAssign generateSchema
        recipient.generatedSchemaName tryAssign generatedSchemaName
        recipient.includes tryAssign includes
        recipient.jsExport tryAssign jsExport
        recipient.languageVersion tryAssign languageVersion
        recipient.nullableFieldStyle tryAssign nullableFieldStyle
        recipient.operationManifest tryAssign operationManifest?.let(::file)
        recipient.operationManifestFormat tryAssign operationManifestFormat
        recipient.operationOutputFile tryAssign operationOutputFile?.let(::file)
        recipient.outputDir tryAssign outputDir?.let(layout.projectDirectory::dir)
        recipient.packageName tryAssign packageName
        recipient.requiresOptInAnnotation tryAssign requiresOptInAnnotation
        recipient.schemaFile tryAssign schemaFile?.let(::file)
        schemaFiles?.toTypedArray()?.let(recipient.schemaFiles::from)
        setSchemaFiles?.let(recipient.schemaFiles::setFrom)
        recipient.sealedClassesForEnumsMatching tryAssign sealedClassesForEnumsMatching
        recipient.sourceFolder tryAssign sourceFolder
        recipient.testDir tryAssign testDir?.let(layout.projectDirectory::dir)
        recipient.useSemanticNaming tryAssign useSemanticNaming
        recipient.warnOnDeprecatedUsages tryAssign warnOnDeprecatedUsages
    }
}
