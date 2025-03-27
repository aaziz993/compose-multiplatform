package gradle.plugins.kotlin.apollo

import com.apollographql.apollo3.compiler.PackageNameGenerator
import com.apollographql.apollo3.gradle.api.Service
import gradle.api.tryAddAll
import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class Service(
    val addJvmOverloads: Boolean? = null,
    val addTypename: String? = null,
    val alwaysGenerateTypesMatching: Set<String>? = null,
    val setAlwaysGenerateTypesMatching: Set<String>? = null,
    val classesForEnumsMatching: Set<String>? = null,
    val setClassesForEnumsMatching: Set<String>? = null,
    val codegenModels: String? = null,
    val debugDir: String? = null,
    val decapitalizeFields: Boolean? = null,
    val excludes: Set<String>? = null,
    val setExcludes: Set<String>? = null,
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
    val setGenerateMethods: Set<String>? = null,
    val generateModelBuilders: Boolean? = null,
    val generateOperationOutput: Boolean? = null,
    val generateOptionalOperationVariables: Boolean? = null,
    val generatePrimitiveTypes: Boolean? = null,
    val generateQueryDocument: Boolean? = null,
    val generateSchema: Boolean? = null,
    val generatedSchemaName: String? = null,
    val includes: Set<String>? = null,
    val setIncludes: Set<String>? = null,
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
    val setSealedClassesForEnumsMatching: Set<String>? = null,
    val sourceFolder: String? = null,
    val testDir: String? = null,
    val useSemanticNaming: Boolean? = null,
    val warnOnDeprecatedUsages: Boolean? = null,
) {

    context(Project)
    fun applyTo(receiver: Service) {
        receiver.addJvmOverloads tryAssign addJvmOverloads
        receiver.addTypename tryAssign addTypename
        receiver.alwaysGenerateTypesMatching tryAddAll alwaysGenerateTypesMatching
        receiver.alwaysGenerateTypesMatching tryAssign setAlwaysGenerateTypesMatching
        receiver.classesForEnumsMatching tryAddAll classesForEnumsMatching
        receiver.classesForEnumsMatching tryAssign setClassesForEnumsMatching
        receiver.codegenModels tryAssign codegenModels
        receiver.debugDir tryAssign debugDir?.let(project.layout.projectDirectory::dir)
        receiver.decapitalizeFields tryAssign decapitalizeFields
        receiver.excludes tryAddAll excludes
        receiver.excludes tryAssign setExcludes
        receiver.failOnWarnings tryAssign failOnWarnings
        receiver.fieldsOnDisjointTypesMustMerge tryAssign fieldsOnDisjointTypesMustMerge
        receiver.flattenModels tryAssign flattenModels
        receiver.generateApolloMetadata tryAssign generateApolloMetadata
        receiver.generateAsInternal tryAssign generateAsInternal
        receiver.generateDataBuilders tryAssign generateDataBuilders
        receiver.generateFragmentImplementations tryAssign generateFragmentImplementations
        receiver.generateInputBuilders tryAssign generateInputBuilders
        receiver.generateKotlinModels tryAssign generateKotlinModels
        receiver.generateMethods tryAddAll generateMethods
        receiver.generateMethods tryAssign setGenerateMethods
        receiver.generateModelBuilders tryAssign generateModelBuilders
        receiver.generateOperationOutput tryAssign generateOperationOutput
        receiver.generateOptionalOperationVariables tryAssign generateOptionalOperationVariables
        receiver.generatePrimitiveTypes tryAssign generatePrimitiveTypes
        receiver.generateQueryDocument tryAssign generateQueryDocument
        receiver.generateSchema tryAssign generateSchema
        receiver.generatedSchemaName tryAssign generatedSchemaName
        receiver.includes tryAddAll  includes
        receiver.includes tryAssign setIncludes
        receiver.jsExport tryAssign jsExport
        receiver.languageVersion tryAssign languageVersion
        receiver.nullableFieldStyle tryAssign nullableFieldStyle
        receiver.operationManifest tryAssign operationManifest?.let(project::file)
        receiver.operationManifestFormat tryAssign operationManifestFormat
        receiver.operationOutputFile tryAssign operationOutputFile?.let(project::file)
        receiver.outputDir tryAssign outputDir?.let(project.layout.projectDirectory::dir)
        receiver.packageName tryAssign packageName
        receiver.requiresOptInAnnotation tryAssign requiresOptInAnnotation
        receiver.schemaFile tryAssign schemaFile?.let(project::file)
        schemaFiles?.toTypedArray()?.let(receiver.schemaFiles::from)
        setSchemaFiles?.let(receiver.schemaFiles::setFrom)
        receiver.sealedClassesForEnumsMatching tryAddAll  sealedClassesForEnumsMatching
        receiver.sealedClassesForEnumsMatching tryAssign setSealedClassesForEnumsMatching
        receiver.sourceFolder tryAssign sourceFolder
        receiver.testDir tryAssign testDir?.let(project.layout.projectDirectory::dir)
        receiver.useSemanticNaming tryAssign useSemanticNaming
        receiver.warnOnDeprecatedUsages tryAssign warnOnDeprecatedUsages
    }
}
