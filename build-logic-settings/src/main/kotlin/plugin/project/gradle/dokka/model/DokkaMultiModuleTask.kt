package plugin.project.gradle.dokka.model

import kotlinx.serialization.Serializable
import org.jetbrains.dokka.DokkaConfiguration

@Serializable
internal data class DokkaMultiModuleTask(
    override val moduleName: String? = null,
    override val moduleVersion: String? = null,
    override val outputDirectory: String? = null,
    override val pluginsConfiguration: List<DokkaConfiguration.PluginConfiguration>? = null,
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

    val fileLayout: DokkaMultiModuleFileLayout? = null,
) : DokkaTask
