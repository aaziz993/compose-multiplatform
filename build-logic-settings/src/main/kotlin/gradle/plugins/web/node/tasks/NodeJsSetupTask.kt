package gradle.plugins.web.node.tasks

import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import gradle.plugins.web.tasks.AbstractSetupTask
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsSetupTask

@Serializable
internal data class NodeJsSetupTask(
    override val destinationHashFileProvider: String? = null,
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
) : AbstractSetupTask<NodeJsSetupTask>() {

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<NodeJsSetupTask>())
}
