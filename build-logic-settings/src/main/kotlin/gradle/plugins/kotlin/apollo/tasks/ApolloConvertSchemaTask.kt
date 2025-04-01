package gradle.plugins.kotlin.apollo.tasks

import com.apollographql.apollo3.gradle.internal.ApolloConvertSchemaTask
import gradle.api.provider.tryAssign
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

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
        applyTo(project.tasks.withType<ApolloConvertSchemaTask>())
}
