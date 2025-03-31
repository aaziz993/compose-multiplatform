package gradle.plugins.knit

import gradle.accessors.files
import gradle.accessors.knit
import gradle.reflect.tryPlus
import gradle.reflect.trySet
import org.gradle.api.Project

internal interface KnitPluginExtension {

    val siteRoot: String?
    val moduleRoots: List<String>?
    val setModuleRoots: List<String>?
    val moduleMarkers: List<String>?
    val setModuleMarkers: List<String>?
    val moduleDocs: String?
    val files: Set<String>?
    val setFiles: Set<String>?
    val rootDir: String?
    val dokkaMultiModuleRoot: String?
    val defaultLineSeparator: String?

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("org.jetbrains.kotlinx.knit") {
            project.knit::siteRoot trySet siteRoot
            project.knit::moduleRoots tryPlus moduleRoots
            project.knit::moduleRoots trySet setModuleRoots
            project.knit::moduleMarkers tryPlus moduleMarkers
            project.knit::moduleMarkers trySet setModuleMarkers
            project.knit::moduleDocs trySet moduleDocs
            project.knit::files trySet setFiles?.let(project::files)
            project.knit::rootDir trySet rootDir?.let(project::file)
            project.knit::dokkaMultiModuleRoot trySet dokkaMultiModuleRoot
            project.knit::defaultLineSeparator trySet defaultLineSeparator
        }
}
