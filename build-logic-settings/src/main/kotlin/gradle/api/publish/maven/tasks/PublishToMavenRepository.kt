package gradle.api.publish.maven.tasks

import gradle.accessors.publishing

import gradle.api.repositories.maven.Maven
import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class PublishToMavenRepository(
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
    val repository: Maven? = null,
    override val publication: String? = null,
) : AbstractPublishToMaven<PublishToMavenRepository>() {

    context(Project)
    override fun applyTo(receiver: PublishToMavenRepository) {
        super.applyTo(receiver)

        PublishToMavenLocal
        repository?.let { repository ->
            if (repository.name.isEmpty() || repository.name == receiver.name) {
                repository.applyTo(receiver.repository)
            }
            else {
                receiver.repository = publishing.repositories.maven {
                    repository.applyTo(this)
                }
            }
        }
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<PublishToMavenRepository>())
}
