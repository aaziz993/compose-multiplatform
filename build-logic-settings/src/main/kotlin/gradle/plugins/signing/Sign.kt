package gradle.plugins.signing

import gradle.accessors.publishing
import gradle.accessors.signing

import gradle.api.getByNameOrAll
import gradle.api.tasks.Task
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.Sign

@Serializable
internal data class Sign(
    override val dependsOn: SortedSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: SortedSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String = "",
    val required: Boolean? = null,
    val signTasks: List<String>? = null,
    val signArtifacts: List<String>? = null,
    val signFiles: List<String>? = null,
    val signClassifierFiles: List<ClassifierFile>? = null,
    val signConfigurations: List<String>? = null,
    val signPublications: List<String>? = null,
) : Task {

        context(Project)
    override fun applyTo(recipient: T) {
        with(project) {
            super.applyTo(named)

            signTasks?.flatMap(tasks::getByNameOrAll)?.let { tasks ->
                signing.sign(*tasks.toTypedArray())
            }

            signConfigurations?.flatMap(configurations::getByNameOrAll)?.let { configurations ->
                signing.sign(*configurations.toTypedArray())
            }

            signPublications?.flatMap(publishing.publications::getByNameOrAll)?.let { publications ->
                signing.sign(*publications.toTypedArray())
            }

            val allArtifacts = configurations.flatMap(Configuration::getAllArtifacts)

            signArtifacts?.mapNotNull { signArtifact ->
                allArtifacts.find { artifact -> artifact.classifier == signArtifact }
            }?.let { signArtifacts ->
                signing.sign(*signArtifacts.toTypedArray())
            }

            signFiles?.map(::file)?.let { files ->
                signing.sign(*files.toTypedArray())
            }

            signClassifierFiles?.forEach { (classifier, files) ->
                signing.sign(classifier, *files.map(::file).toTypedArray())
            }
        }
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<Sign>())
}
