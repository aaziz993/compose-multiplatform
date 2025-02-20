package plugin.project.kotlin.apollo

import com.apollographql.apollo3.compiler.OperationIdGenerator
import com.apollographql.apollo3.compiler.PackageNameGenerator
import com.apollographql.apollo3.compiler.hooks.ApolloCompilerJavaHooks
import com.apollographql.apollo3.gradle.internal.ApolloPlugin
import gradle.moduleProperties
import gradle.apollo
import gradle.tryAssign
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.withType
import plugin.project.kotlin.apollo.model.Service

internal fun Project.configureApolloExtension() =
    plugins.withType<ApolloPlugin> {
        moduleProperties.settings.kotlin.apollo.let { apollo ->
            apollo {
                apollo.processors?.forEach { (schema, service, packageName) ->
                    apolloKspProcessor(file(schema), service, packageName)
                }

                apollo.androidServices?.forEach { androidService ->
                    createAllAndroidVariantServices(androidService.sourceFolder, androidService.nameSuffix) {
                        androidService.service?.let { service -> configureFrom(service) }
                    }
                }

                apollo.kotlinService?.forEach { kotlinService ->
                    createAllKotlinSourceSetServices(kotlinService.sourceFolder, kotlinService.nameSuffix) {
                        kotlinService.service?.let { service -> configureFrom(service) }
                    }
                }

                apollo.services?.forEach { service ->
                    service(service.name) {
                        configureFrom(service)
                    }
                }

                generateSourcesDuringGradleSync tryAssign apollo.generateSourcesDuringGradleSync
                linkSqlite tryAssign apollo.linkSqlite
            }
        }
    }

context(Project)
private fun com.apollographql.apollo3.gradle.api.Service.configureFrom(config: Service) {
    addJvmOverloads tryAssign config.addJvmOverloads
    addTypename tryAssign config.addTypename
    alwaysGenerateTypesMatching tryAssign config.alwaysGenerateTypesMatching
    classesForEnumsMatching tryAssign config.classesForEnumsMatching
    codegenModels tryAssign config.codegenModels
    compilerJavaHooks tryAssign config.compilerJavaHooks
    debugDir tryAssign config.debugDir?.let(layout.projectDirectory::dir)
    decapitalizeFields tryAssign config.decapitalizeFields
    excludes tryAssign config.excludes
    failOnWarnings tryAssign config.failOnWarnings
    fieldsOnDisjointTypesMustMerge tryAssign config.fieldsOnDisjointTypesMustMerge
    flattenModels tryAssign config.flattenModels
    generateApolloMetadata tryAssign config.generateApolloMetadata
    generateAsInternal tryAssign config.generateAsInternal
    generateDataBuilders tryAssign config.generateDataBuilders
    generateFragmentImplementations tryAssign config.generateFragmentImplementations
    generateInputBuilders tryAssign config.generateInputBuilders
    generateKotlinModels tryAssign config.generateKotlinModels
    generateMethods tryAssign config.generateMethods
    generateModelBuilders tryAssign config.generateModelBuilders
    generateOperationOutput tryAssign config.generateOperationOutput
    generateOptionalOperationVariables tryAssign config.generateOptionalOperationVariables
    generatePrimitiveTypes tryAssign config.generatePrimitiveTypes
    generateQueryDocument tryAssign config.generateQueryDocument
    generateSchema tryAssign config.generateSchema
    generatedSchemaName tryAssign config.generatedSchemaName
    includes tryAssign config.includes
    jsExport tryAssign config.jsExport
    languageVersion tryAssign config.languageVersion
    nullableFieldStyle tryAssign config.nullableFieldStyle
    operationManifest tryAssign config.operationManifest?.let(::file)
    operationManifestFormat tryAssign config.operationManifestFormat
    operationOutputFile tryAssign config.operationOutputFile?.let(::file)
    outputDir tryAssign config.outputDir?.let(layout.projectDirectory::dir)
    packageName tryAssign config.packageName
    requiresOptInAnnotation tryAssign config.requiresOptInAnnotation
    schemaFile tryAssign config.schemaFile?.let(::file)
    config.schemaFiles?.let(schemaFiles::setFrom)
    sealedClassesForEnumsMatching tryAssign config.sealedClassesForEnumsMatching
    sourceFolder tryAssign config.sourceFolder
    testDir tryAssign config.testDir?.let(layout.projectDirectory::dir)
    useSemanticNaming tryAssign config.useSemanticNaming
    warnOnDeprecatedUsages tryAssign config.warnOnDeprecatedUsages
}
