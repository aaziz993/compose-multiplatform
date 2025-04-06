package gradle.plugins.karakum.tasks

import gradle.api.provider.tryAssign
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import klib.data.type.serialization.json.serializer.SerializableAnyMap
import io.github.sgrishchenko.karakum.gradle.plugin.tasks.KarakumCopy
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class KarakumCopy(
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
    val extensionSource: String? = null,
    val destinationDirectory: String? = null,
) : DefaultTask<KarakumCopy>() {

    context(Project)
    override fun applyTo(receiver: KarakumCopy) =
        project.pluginManager.withPlugin("io.github.sgrishchenko.karakum") {
            super.applyTo(receiver)

            receiver.extensionSource tryAssign extensionSource?.let(project.layout.projectDirectory::dir)?.asFileTree
            receiver.destinationDirectory tryAssign destinationDirectory?.let(project.layout.projectDirectory::dir)
        }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<KarakumCopy>())
}
