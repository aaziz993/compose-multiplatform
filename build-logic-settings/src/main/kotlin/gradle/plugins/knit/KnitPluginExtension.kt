package gradle.plugins.knit

import gradle.accessors.id
import gradle.accessors.knit
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.api.trySet
import org.gradle.api.Project

internal interface KnitPluginExtension {

    val siteRoot: String?
    val moduleRoots: List<String>?
    val moduleMarkers: List<String>?
    val moduleDocs: String?
    val files: Set<String>?
    val setFiles: Set<String>?
    val rootDir: String?
    val dokkaMultiModuleRoot: String?
    val defaultLineSeparator: String?

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("knit").id) {
            project. knit::siteRoot trySet siteRoot
            project. knit.moduleRoots = moduleRoots ?: (project.settings.projectProperties.includes.orEmpty().toList() + listOf("."))
            project. knit.moduleMarkers = moduleMarkers ?: listOf("build.gradle", "build.gradle.kts", "project.yaml")
            project. knit::moduleDocs trySet moduleDocs
            project. knit::files trySet setFiles?.toTypedArray()?.let(project::files)
            project.knit::rootDir trySet rootDir?.let(project::file)
            project. knit::dokkaMultiModuleRoot trySet dokkaMultiModuleRoot
            project.knit::defaultLineSeparator trySet defaultLineSeparator
        }
}
