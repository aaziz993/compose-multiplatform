package plugin.project.gradle.dokka.model

import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class DokkaTask(
    override val name: String = "",
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    override val moduleName: String? = null,
    override val moduleVersion: String? = null,
    override val outputDirectory: String? = null,
    override val pluginsConfiguration: List<PluginConfiguration>? = null,
    override val pluginsMapConfiguration: Map<String, String>? = null,
    override val suppressObviousFunctions: Boolean? = null,
    override val suppressInheritedMembers: Boolean? = null,
    override val offlineMode: Boolean? = null,
    override val failOnWarning: Boolean? = null,
    override val cacheRoot: String? = null,
) : AbstractDokkaTask() {

    context(Project)
    fun applyTo() {
        tasks.withType<org.jetbrains.dokka.gradle.DokkaTask> {
            super.applyTo(this)
        }
    }
}
