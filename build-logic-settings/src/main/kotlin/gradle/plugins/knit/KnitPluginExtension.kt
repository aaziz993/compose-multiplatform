package gradle.plugins.knit

import gradle.accessors.id
import gradle.accessors.knit
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.trySet
import org.gradle.api.Project

internal interface KnitPluginExtension {

    val siteRoot: String?
    val moduleRoots: List<String>?
    val moduleMarkers: List<String>?
    val moduleDocs: String?
    val files: List<String>?
    val rootDir: String?
    val dokkaMultiModuleRoot: String?
    val defaultLineSeparator: String?

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("knit").id) {
            knit::siteRoot trySet siteRoot
            knit::moduleRoots trySet moduleRoots
            knit::moduleMarkers trySet moduleMarkers
            knit::moduleDocs trySet moduleDocs
            knit::files trySet files?.let { files(*it.toTypedArray()) }
            knit::rootDir trySet rootDir?.let(::file)
            knit::dokkaMultiModuleRoot trySet dokkaMultiModuleRoot
            knit::defaultLineSeparator trySet defaultLineSeparator
        }
}
