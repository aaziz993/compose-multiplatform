package gradle.model.gradle.knit

import gradle.id
import gradle.knit
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import gradle.trySet
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
