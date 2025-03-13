package gradle.api.publish.maven.tasks

import gradle.accessors.publishing
import gradle.collection.SerializableAnyMap
import gradle.repositories.maven.Maven
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.ArtifactRepository
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.kotlin.dsl.withType

@kotlinx.serialization.Serializable
internal data class PublishToMavenRepository(
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    override val name: String = "",
    val repository: Maven? = null,
) : AbstractPublishToMaven() {

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as PublishToMavenRepository
        PublishToMavenLocal
        repository?.let { repository ->
            if (repository.name.isEmpty() || repository.name == named.name) {
                repository.applyTo(named.repository as ArtifactRepository)
            }
            else {
                named.repository = publishing.repositories.maven {
                    repository.applyTo(this as ArtifactRepository)
                }
            }
        }
    }

    context(Project)
    override fun applyTo() =
        super.applyTo(tasks.withType<PublishToMavenRepository>())
}
