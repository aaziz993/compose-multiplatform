package gradle.plugins.dokka

import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.gradle.tasks.DokkaGenerateTask

@Serializable
internal data class DokkaGenerateTask(
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
    /**
     * Directory containing the generation result. The content and structure depends on whether
     * the task generates a Dokka Module or a Dokka Publication.
     */
    val outputDirectory: String? = null,
    /**
     * Classpath required to run Dokka Generator.
     *
     * Contains the Dokka Generator, Dokka plugins, and any transitive dependencies.
     */
    val runtimeClasspath: List<String>? = null,
    /** @see org.jetbrains.dokka.gradle.formats.DokkaPublication.cacheRoot */
    val cacheDirectory: String? = null,
    /** @see org.jetbrains.dokka.gradle.formats.DokkaPublication.enabled */
    val publicationEnabled: Boolean? = null,
    val generator: DokkaGeneratorParametersSpec? = null,
    /**
     * Control how Dokka Gradle Plugin launches Dokka Generator.
     *
     * Defaults to [org.jetbrains.dokka.gradle.DokkaExtension.dokkaGeneratorIsolation].
     *
     * @see org.jetbrains.dokka.gradle.DokkaExtension.dokkaGeneratorIsolation
     * @see org.jetbrains.dokka.gradle.workers.ProcessIsolation
     */
    val workerIsolation: WorkerIsolation? = null,
    /**
     * All [org.jetbrains.dokka.DokkaGenerator] logs will be saved to this file.
     * This can be used for debugging purposes.
     */
    val workerLogFile: String? = null,
    /**
     * The [DokkaConfiguration] by Dokka Generator can be saved to a file for debugging purposes.
     * To disable this behaviour set this property to `null`.
     */
    val dokkaConfigurationJsonFile: String? = null,
    /**
     * Completely override the default Dokka configuration with JSON encoded
     * Dokka Configuration.
     *
     * This should only be used for local debugging.
     */
    val overrideJsonConfig: String? = null,
) : DokkaBaseTask() {

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as DokkaGenerateTask

        named.outputDirectory tryAssign outputDirectory?.let(layout.projectDirectory::dir)
        runtimeClasspath?.let(named.runtimeClasspath::setFrom)
        named.cacheDirectory tryAssign cacheDirectory?.let(layout.projectDirectory::dir)
        named.publicationEnabled tryAssign publicationEnabled
        generator?.applyTo(named.generator)
        named.workerIsolation tryAssign workerIsolation?.toWorkerIsolation()
        named.workerLogFile tryAssign workerLogFile?.let(::file)
        named.dokkaConfigurationJsonFile tryAssign dokkaConfigurationJsonFile?.let(::file)
        named.overrideJsonConfig tryAssign overrideJsonConfig
    }

    context(Project)
    override fun applyTo() =
        super.applyTo(tasks.withType<DokkaGenerateTask>())
}
