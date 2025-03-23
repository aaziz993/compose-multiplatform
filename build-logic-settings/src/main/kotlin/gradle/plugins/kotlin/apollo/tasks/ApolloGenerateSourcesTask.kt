package gradle.plugins.kotlin.apollo.tasks

public abstract class ApolloGenerateSourcesTask public constructor() : com.apollographql.apollo3.gradle.internal.ApolloGenerateSourcesBase {
    public companion object {
        internal final fun scalarMapping(scalarTypeMapping: Map<String, String>, scalarAdapterMapping: Map<String, String>): Map<String, com.apollographql.apollo3.compiler.ScalarInfo> { /* compiled code */ }
    }

    val addTypename: String?=null,


    val alwaysGenerateTypesMatching: SetString?=null,


    val codegenModels: String?=null,


    val decapitalizeFields: Boolean?=null,


    val failOnWarnings: Boolean?=null,


    val fieldsOnDisjointTypesMustMerge: Boolean?=null,


    val flattenModels: Boolean?=null,


    val generateDataBuilders: Boolean?=null,


    val graphqlFiles: Set<String>?=null,
val setGraphqlFiles: Set<String>?=null,


    val projectPath: String?=null,


    val scalarAdapterMapping: Map<String, String>


    val scalarTypeMapping: Map<String, String>


    val schemaFiles: Set<String>?=null,
val setSchemaFiles: Set<String>?=null,


    val targetLanguage: TargetLanguage?=null,


    val warnOnDeprecatedUsages: Boolean?=null,



}

