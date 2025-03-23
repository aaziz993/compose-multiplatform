package gradle.plugins.kotlin.apollo.tasks

import org.gradle.kotlin.dsl.withType
import com.apollographql.apollo3.gradle.internal.ApolloGenerateIrTask
import kotlinx.serialization.Serializable
import gradle.api.tasks.DefaultTask
import org.gradle.api.Project
import gradle.collection.SerializableAnyMap
import gradle.api.tasks.applyTo
import gradle.api.tryAssign

@Serializable
internal data class ApolloGenerateIrTask(
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
    val codegenSchemas: Set<String>? = null,
    val setCodegenSchemas: Set<String>? = null,
    val decapitalizeFields: Boolean? = null,
    val failOnWarnings: Boolean? = null,
    val fieldsOnDisjointTypesMustMerge: Boolean? = null,
    val flattenModels: Boolean? = null,
    val generateOptionalOperationVariables: Boolean? = null,
    val graphqlFiles: Set<String>? = null,
    val setGraphqlFiles: Set<String>? = null,
    val outputFile: String? = null,
    val upstreamIrFiles: Set<String>? = null,
    val setUpstreamIrFiles: Set<String>? = null,
    val warnOnDeprecatedUsages: Boolean? = null,
) : DefaultTask<ApolloGenerateIrTask>() {

    context(Project)
    override fun applyTo(receiver: ApolloGenerateIrTask) {
        super.applyTo(receiver)

        receiver.addTypename tryAssign addTypename

        receiver.alwaysGenerateTypesMatching tryAssign alwaysGenerateTypesMatching
            ?.let { alwaysGenerateTypesMatching ->
                receiver.alwaysGenerateTypesMatching.get() + alwaysGenerateTypesMatching
            }

        receiver.alwaysGenerateTypesMatching tryAssign setAlwaysGenerateTypesMatching
        codegenSchemas?.toTypedArray()?.let(receiver.codegenSchemas::from)
        setCodegenSchemas?.let(receiver.codegenSchemas::setFrom)
        receiver.decapitalizeFields tryAssign decapitalizeFields
        receiver.failOnWarnings tryAssign failOnWarnings
        receiver.fieldsOnDisjointTypesMustMerge tryAssign fieldsOnDisjointTypesMustMerge
        receiver.flattenModels tryAssign flattenModels
        receiver.generateOptionalOperationVariables tryAssign generateOptionalOperationVariables
        graphqlFiles?.toTypedArray()?.let(receiver.graphqlFiles::from)
        setGraphqlFiles?.let(receiver.graphqlFiles::setFrom)
        receiver.outputFile tryAssign outputFile?.let(::file)
        upstreamIrFiles?.toTypedArray()?.let(receiver.upstreamIrFiles::from)
        setUpstreamIrFiles?.toTypedArray()?.let(receiver.upstreamIrFiles::setFrom)
        receiver.warnOnDeprecatedUsages tryAssign warnOnDeprecatedUsages
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<ApolloGenerateIrTask>())
}
