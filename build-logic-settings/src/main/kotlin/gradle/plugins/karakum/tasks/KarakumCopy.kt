package gradle.plugins.karakum.tasks

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
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
        pluginManager.withPlugin(settings.libs.plugins.plugin("karakum").id) {
            super.applyTo(receiver)

            receiver.extensionSource tryAssign extensionSource?.let(layout.projectDirectory::dir)?.asFileTree
            receiver.destinationDirectory tryAssign destinationDirectory?.let(layout.projectDirectory::dir)
        }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<KarakumCopy>())
}
