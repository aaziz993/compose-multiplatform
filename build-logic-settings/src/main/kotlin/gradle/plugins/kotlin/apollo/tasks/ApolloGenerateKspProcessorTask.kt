package gradle.plugins.kotlin.apollo.tasks

import com.apollographql.apollo3.gradle.internal.ApolloGenerateKspProcessorTask
import gradle.api.file.tryAssign
import gradle.api.provider.tryAssign
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class ApolloGenerateKspProcessorTask(
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
    val outputJar: String? = null,
    val packageName: String? = null,
    val schema: String? = null,
    val serviceName: String? = null,
) : DefaultTask<ApolloGenerateKspProcessorTask>() {

    context(Project)
    override fun applyTo(receiver: ApolloGenerateKspProcessorTask) {
        super.applyTo(receiver)

        receiver.outputJar tryAssign outputJar?.let(project::file)
        receiver.packageName tryAssign packageName
        receiver.schema tryAssign schema?.let(project::file)
        receiver.serviceName tryAssign serviceName
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<ApolloGenerateKspProcessorTask>())
}
