package gradle.plugins.kotlin.apollo.tasks

import org.gradle.kotlin.dsl.withType
import com.apollographql.apollo3.gradle.internal.ApolloConvertSchemaTask
import kotlinx.serialization.Serializable
import gradle.api.tasks.DefaultTask
import org.gradle.api.Project
import gradle.collection.SerializableAnyMap
import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.api.trySet

@Serializable
internal data class ApolloConvertSchemaTask(
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
    val from: String? = null,
    val projectRootDir: String? = null,
    val to: String? = null,
) : DefaultTask<ApolloConvertSchemaTask>() {

    context(Project)
    override fun applyTo(receiver: ApolloConvertSchemaTask) {
        super.applyTo(receiver)

        receiver.from tryAssign from
        receiver::projectRootDir trySet projectRootDir
        receiver.to tryAssign to
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<ApolloConvertSchemaTask>())
}
