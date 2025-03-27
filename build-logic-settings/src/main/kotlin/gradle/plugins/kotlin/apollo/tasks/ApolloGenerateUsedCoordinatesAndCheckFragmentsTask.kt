package gradle.plugins.kotlin.apollo.tasks

import com.apollographql.apollo3.gradle.internal.ApolloGenerateUsedCoordinatesAndCheckFragmentsTask
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class ApolloGenerateUsedCoordinatesAndCheckFragmentsTask(
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
    val downStreamIrOperations: Set<String>? = null,
    val setDownStreamIrOperations: Set<String>? = null,
    val irOperations: String? = null,
    val outputFile: String? = null,
) : DefaultTask<ApolloGenerateUsedCoordinatesAndCheckFragmentsTask>() {

    context(Project)
    override fun applyTo(receiver: ApolloGenerateUsedCoordinatesAndCheckFragmentsTask) {
        super.applyTo(receiver)

        receiver.downStreamIrOperations tryFrom downStreamIrOperations
        receiver.downStreamIrOperations trySetFrom setDownStreamIrOperations
        receiver.irOperations tryAssign irOperations?.let(project::file)
        receiver.outputFile tryAssign outputFile?.let(project::file)
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<ApolloGenerateUsedCoordinatesAndCheckFragmentsTask>())
}
