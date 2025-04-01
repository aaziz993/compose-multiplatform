package gradle.plugins.kotlin.targets.web.npm.tasks

import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import klib.data.type.collection.SerializableAnyMap
import klib.data.type.collection.tryAddAll
import klib.data.type.collection.trySet
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
    override fun applyTo(receiver: KotlinNpmInstallTask) {
        super.applyTo(receiver)

        receiver.args tryAddAll args
        receiver.args trySet setArgs
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<KotlinNpmInstallTask>())
}
