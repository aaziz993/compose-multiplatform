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
import io.github.sgrishchenko.karakum.gradle.plugin.tasks.KarakumConfig
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class KarakumConfig(
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
    val configFile: String? = null,
    val destinationFile: String? = null,
) : DefaultTask<KarakumConfig>() {

    context(Project)
    override fun applyTo(receiver: KarakumConfig) =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("karakum").id) {
            super.applyTo(receiver)

            receiver.configFile tryAssign configFile?.let(project::file)
            receiver.destinationFile tryAssign destinationFile?.let(project::file)
        }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<KarakumConfig>())
}
