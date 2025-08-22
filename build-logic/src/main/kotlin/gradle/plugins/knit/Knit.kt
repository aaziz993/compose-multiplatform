package gradle.plugins.knit

import gradle.api.project.moduleName
import gradle.api.project.settings
import kotlinx.knit.KnitPluginExtension
import org.gradle.api.Project
import org.jetbrains.dokka.DokkaConfiguration

context(project: Project)
public fun KnitPluginExtension.moduleRootsFromIncludes(): Unit =
    project.pluginManager.withPlugin("org.jetbrains.kotlinx.knit") {
        moduleRoots = (project.settings.gradle.rootProject.subprojects.map(Project::moduleName) + ".")
    }
