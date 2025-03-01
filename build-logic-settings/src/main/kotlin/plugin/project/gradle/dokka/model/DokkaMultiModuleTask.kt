package plugin.project.gradle.dokka.model

import gradle.serialization.serializer.AnySerializer
import gradle.tryAssign
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.internal.InternalDokkaGradlePluginApi
import plugin.project.kotlin.model.configure

@Serializable
@SerialName("dokkaMultiModuleTask")
internal data class DokkaMultiModuleTask(
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
    /**
     * List of Markdown files that contain
     * [module and package documentation](https://kotlinlang.org/docs/dokka-module-and-package-docs.html).
     *
     * Contents of specified files will be parsed and embedded into documentation as module and package descriptions.
     *
     * Example of such a file:
     *
     * ```markdown
     * # Module kotlin-demo
     *
     * The module shows the Dokka usage.
     *
     * # Package org.jetbrains.kotlin.demo
     *
     * Contains assorted useful stuff.
     *
     * ## Level 2 heading
     *
     * Text after this heading is also part of documentation for `org.jetbrains.kotlin.demo`
     *
     * # Package org.jetbrains.kotlin.demo2
     *
     * Useful stuff in another package.
     * ```
     */
    val includes: List<String>? = null,

    val fileLayout: DokkaMultiModuleFileLayout? = null
) : AbstractDokkaTask() {

    context(Project)
    @OptIn(InternalDokkaGradlePluginApi::class)
    override fun applyTo() {
        tasks.withType<org.jetbrains.dokka.gradle.DokkaMultiModuleTask> {
            super.applyTo()
            tasks.configure {
                this as org.jetbrains.dokka.gradle.DokkaMultiModuleTask

                this@DokkaMultiModuleTask.includes?.let(includes::setFrom)
                fileLayout tryAssign this@DokkaMultiModuleTask.fileLayout?.let(DokkaMultiModuleFileLayout::toDokkaMultiModuleFileLayout)
            }
        }
    }
}
