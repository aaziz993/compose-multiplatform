package plugin.project.kotlin.apollo.model

public interface Service {
    public abstract val addJvmOverloads: org.gradle.api.provider.Property<kotlin.Boolean>

    public abstract val addTypename: org.gradle.api.provider.Property<kotlin.String>

    public abstract val alwaysGenerateTypesMatching: org.gradle.api.provider.SetProperty<kotlin.String>

    public abstract val classesForEnumsMatching: org.gradle.api.provider.ListProperty<kotlin.String>

    public abstract val codegenModels: org.gradle.api.provider.Property<kotlin.String>

    @com.apollographql.apollo3.annotations.ApolloExperimental public abstract val compilerJavaHooks: org.gradle.api.provider.ListProperty<com.apollographql.apollo3.compiler.hooks.ApolloCompilerJavaHooks>

    @com.apollographql.apollo3.annotations.ApolloExperimental public abstract val compilerKotlinHooks: org.gradle.api.provider.ListProperty<com.apollographql.apollo3.compiler.hooks.ApolloCompilerKotlinHooks>

    public abstract val debugDir: org.gradle.api.file.DirectoryProperty

    public abstract val decapitalizeFields: org.gradle.api.provider.Property<kotlin.Boolean>

    public abstract val excludes: org.gradle.api.provider.ListProperty<kotlin.String>

    public abstract val failOnWarnings: org.gradle.api.provider.Property<kotlin.Boolean>

    public abstract val fieldsOnDisjointTypesMustMerge: org.gradle.api.provider.Property<kotlin.Boolean>

    public abstract val flattenModels: org.gradle.api.provider.Property<kotlin.Boolean>

    public abstract val generateApolloMetadata: org.gradle.api.provider.Property<kotlin.Boolean>

    public abstract val generateAsInternal: org.gradle.api.provider.Property<kotlin.Boolean>

    @com.apollographql.apollo3.annotations.ApolloExperimental public abstract val generateDataBuilders: org.gradle.api.provider.Property<kotlin.Boolean>

    public abstract val generateFragmentImplementations: org.gradle.api.provider.Property<kotlin.Boolean>

    @com.apollographql.apollo3.annotations.ApolloExperimental public abstract val generateInputBuilders: org.gradle.api.provider.Property<kotlin.Boolean>

    public abstract val generateKotlinModels: org.gradle.api.provider.Property<kotlin.Boolean>

    public abstract val generateMethods: org.gradle.api.provider.ListProperty<kotlin.String>

    @com.apollographql.apollo3.annotations.ApolloExperimental public abstract val generateModelBuilders: org.gradle.api.provider.Property<kotlin.Boolean>

    public abstract val generateOperationOutput: org.gradle.api.provider.Property<kotlin.Boolean>

    public abstract val generateOptionalOperationVariables: org.gradle.api.provider.Property<kotlin.Boolean>

    public abstract val generatePrimitiveTypes: org.gradle.api.provider.Property<kotlin.Boolean>

    public abstract val generateQueryDocument: org.gradle.api.provider.Property<kotlin.Boolean>

    public abstract val generateSchema: org.gradle.api.provider.Property<kotlin.Boolean>

    public abstract val generatedSchemaName: org.gradle.api.provider.Property<kotlin.String>

    public abstract val includes: org.gradle.api.provider.ListProperty<kotlin.String>

    public abstract val jsExport: org.gradle.api.provider.Property<kotlin.Boolean>

    public abstract val languageVersion: org.gradle.api.provider.Property<kotlin.String>

    public abstract val name: kotlin.String

    public abstract val nullableFieldStyle: org.gradle.api.provider.Property<kotlin.String>

    public abstract val operationIdGenerator: org.gradle.api.provider.Property<com.apollographql.apollo3.compiler.OperationIdGenerator>

    public abstract val operationManifest: org.gradle.api.file.RegularFileProperty

    public abstract val operationManifestFormat: org.gradle.api.provider.Property<kotlin.String>

    public abstract val operationOutputFile: org.gradle.api.file.RegularFileProperty

    public abstract val operationOutputGenerator: org.gradle.api.provider.Property<com.apollographql.apollo3.compiler.OperationOutputGenerator>

    public abstract val outputDir: org.gradle.api.file.DirectoryProperty

    public abstract val packageName: org.gradle.api.provider.Property<kotlin.String>

    public abstract val packageNameGenerator: org.gradle.api.provider.Property<com.apollographql.apollo3.compiler.PackageNameGenerator>

    @com.apollographql.apollo3.annotations.ApolloExperimental public abstract val requiresOptInAnnotation: org.gradle.api.provider.Property<kotlin.String>

    public abstract val schemaFile: org.gradle.api.file.RegularFileProperty

    public abstract val schemaFiles: org.gradle.api.file.ConfigurableFileCollection

    public abstract val sealedClassesForEnumsMatching: org.gradle.api.provider.ListProperty<kotlin.String>

    public abstract val sourceFolder: org.gradle.api.provider.Property<kotlin.String>

    public abstract val testDir: org.gradle.api.file.DirectoryProperty

    public abstract val useSemanticNaming: org.gradle.api.provider.Property<kotlin.Boolean>

    public abstract val warnOnDeprecatedUsages: org.gradle.api.provider.Property<kotlin.Boolean>

    public abstract fun dependsOn(dependencyNotation: kotlin.Any): kotlin.Unit

    public abstract fun introspection(configure: org.gradle.api.Action<in com.apollographql.apollo3.gradle.api.Introspection>): kotlin.Unit

    public abstract fun isADependencyOf(dependencyNotation: kotlin.Any): kotlin.Unit

    public abstract fun mapScalar(graphQLName: kotlin.String, targetName: kotlin.String): kotlin.Unit

    public abstract fun mapScalar(graphQLName: kotlin.String, targetName: kotlin.String, expression: kotlin.String): kotlin.Unit

    public abstract fun mapScalarToJavaBoolean(graphQLName: kotlin.String): kotlin.Unit

    public abstract fun mapScalarToJavaDouble(graphQLName: kotlin.String): kotlin.Unit

    public abstract fun mapScalarToJavaFloat(graphQLName: kotlin.String): kotlin.Unit

    public abstract fun mapScalarToJavaInteger(graphQLName: kotlin.String): kotlin.Unit

    public abstract fun mapScalarToJavaLong(graphQLName: kotlin.String): kotlin.Unit

    public abstract fun mapScalarToJavaObject(graphQLName: kotlin.String): kotlin.Unit

    public abstract fun mapScalarToJavaString(graphQLName: kotlin.String): kotlin.Unit

    public abstract fun mapScalarToKotlinAny(graphQLName: kotlin.String): kotlin.Unit

    public abstract fun mapScalarToKotlinBoolean(graphQLName: kotlin.String): kotlin.Unit

    public abstract fun mapScalarToKotlinDouble(graphQLName: kotlin.String): kotlin.Unit

    public abstract fun mapScalarToKotlinFloat(graphQLName: kotlin.String): kotlin.Unit

    public abstract fun mapScalarToKotlinInt(graphQLName: kotlin.String): kotlin.Unit

    public abstract fun mapScalarToKotlinLong(graphQLName: kotlin.String): kotlin.Unit

    public abstract fun mapScalarToKotlinString(graphQLName: kotlin.String): kotlin.Unit

    public abstract fun mapScalarToUpload(graphQLName: kotlin.String): kotlin.Unit

    public abstract fun operationManifestConnection(action: org.gradle.api.Action<in com.apollographql.apollo3.gradle.api.Service.OperationManifestConnection>): kotlin.Unit

    public abstract fun operationOutputConnection(action: org.gradle.api.Action<in com.apollographql.apollo3.gradle.api.Service.OperationOutputConnection>): kotlin.Unit

    public abstract fun outputDirConnection(action: org.gradle.api.Action<in com.apollographql.apollo3.gradle.api.Service.DirectoryConnection>): kotlin.Unit

    public abstract fun packageNamesFromFilePaths(rootPackageName: kotlin.String? = COMPILED_CODE): kotlin.Unit

    public abstract fun registerOperations(configure: org.gradle.api.Action<in com.apollographql.apollo3.gradle.api.RegisterOperationsConfig>): kotlin.Unit

    public abstract fun registry(configure: org.gradle.api.Action<in com.apollographql.apollo3.gradle.api.Registry>): kotlin.Unit

    public abstract fun srcDir(directory: kotlin.Any): kotlin.Unit

    public abstract fun usedCoordinates(file: java.io.File): kotlin.Unit

    public abstract fun usedCoordinates(file: kotlin.String): kotlin.Unit

    public interface DirectoryConnection {
        public abstract val outputDir: org.gradle.api.provider.Provider<org.gradle.api.file.Directory>

        public abstract val task: org.gradle.api.tasks.TaskProvider<out org.gradle.api.Task>

        public abstract fun connectToAllAndroidVariants(): kotlin.Unit

        public abstract fun connectToAndroidSourceSet(name: kotlin.String): kotlin.Unit

        public abstract fun connectToAndroidVariant(variant: kotlin.Any): kotlin.Unit

        public abstract fun connectToJavaSourceSet(name: kotlin.String): kotlin.Unit

        public abstract fun connectToKotlinSourceSet(name: kotlin.String): kotlin.Unit
    }

    public final class OperationManifestConnection public constructor(task: org.gradle.api.tasks.TaskProvider<out org.gradle.api.Task>, manifest: org.gradle.api.provider.Provider<org.gradle.api.file.RegularFile>) {
        public final val manifest: org.gradle.api.provider.Provider<org.gradle.api.file.RegularFile> /* compiled code */

        public final val task: org.gradle.api.tasks.TaskProvider<out org.gradle.api.Task> /* compiled code */
    }

    public final class OperationOutputConnection public constructor(task: org.gradle.api.tasks.TaskProvider<out org.gradle.api.Task>, operationOutputFile: org.gradle.api.provider.Provider<org.gradle.api.file.RegularFile>) {
        public final val operationOutputFile: org.gradle.api.provider.Provider<org.gradle.api.file.RegularFile> /* compiled code */

        public final val task: org.gradle.api.tasks.TaskProvider<out org.gradle.api.Task> /* compiled code */
    }
}

