package gradle.plugins.web

import org.gradle.kotlin.dsl.withType
import gradle.api.tasks.Task
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask

@Serializable
internal data class KotlinNpmInstallTask(
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
    val args: List<String>? = null,
) : Task {

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as KotlinNpmInstallTask

        args?.let(named.args::addAll)
    }

    context(Project)
    override fun applyTo() =
        super.applyTo(tasks.withType<KotlinNpmInstallTask>())
}
