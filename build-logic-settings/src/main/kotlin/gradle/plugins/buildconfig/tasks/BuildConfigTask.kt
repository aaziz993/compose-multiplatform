package gradle.plugins.buildconfig.tasks

import com.github.gmazzo.gradle.plugins.BuildConfigTask
import gradle.accessors.buildConfig
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import gradle.plugins.buildconfig.generator.BuildConfigGenerator
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class BuildConfigTask(
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
    val specs: Set<String>? = null,
    val generator: BuildConfigGenerator<*>? = null,
    val outputDir: String? = null,
) : DefaultTask<BuildConfigTask>() {

    context(Project)
    override fun applyTo(receiver: BuildConfigTask) {
        super.applyTo(receiver)

        receiver.specs tryAssign specs?.map(buildConfig.sourceSets::getByName)
        receiver.generator tryAssign generator?.toBuildConfigGenerator()
        receiver.outputDir tryAssign outputDir?.let(layout.projectDirectory::dir)
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<BuildConfigTask>())
}
