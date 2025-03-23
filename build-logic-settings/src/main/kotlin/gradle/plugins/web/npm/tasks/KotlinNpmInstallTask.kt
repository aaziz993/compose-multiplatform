package gradle.plugins.web.npm.tasks

import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import gradle.collection.act
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask

@Serializable
internal data class KotlinNpmInstallTask(
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
    val args: List<String>? = null,
    val setArgs: List<String>? = null,
) : DefaultTask<KotlinNpmInstallTask>() {

    context(Project)
    override fun applyTo(recipient: KotlinNpmInstallTask) {
        super.applyTo(recipient)

        args?.let(recipient.args::addAll)
        setArgs?.act(recipient.args::clear)?.let(recipient.args::addAll)
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<KotlinNpmInstallTask>())
}
