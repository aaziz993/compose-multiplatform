package gradle.plugins.knit

import gradle.api.project.settings
import klib.data.type.primitives.string.addPrefix
import kotlinx.knit.KnitPluginExtension
import org.gradle.api.Project

context(project: Project)
public fun KnitPluginExtension.moduleRootsFromIncludes(): Unit =
    project.pluginManager.withPlugin("org.jetbrains.kotlinx.knit") {
        moduleRoots =
            (project.settings.gradle.rootProject.subprojects.map { project ->
                project.path.replace(":", "/")
            } + ".")
    }
