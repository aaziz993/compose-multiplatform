package plugin.project.gradle.dokka.model

import gradle.tryAssign
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.internal.InternalDokkaGradlePluginApi

internal interface DokkaMultiModuleTask : DokkaTask {

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
    val includes: List<String>?

    val fileLayout: DokkaMultiModuleFileLayout?

    context(Project)
    @OptIn(InternalDokkaGradlePluginApi::class)
    fun applyTo(task: DokkaMultiModuleTask) {
        super.applyTo(task)
        includes?.let(task.includes::setFrom)
        task.fileLayout tryAssign fileLayout?.let(DokkaMultiModuleFileLayout::toDokkaMultiModuleFileLayout)
    }
}
