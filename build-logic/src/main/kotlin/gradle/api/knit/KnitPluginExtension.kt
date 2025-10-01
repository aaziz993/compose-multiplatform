package gradle.api.knit

import gradle.api.project.settings
import kotlinx.knit.KnitPluginExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

context(project: Project)
public fun KnitPluginExtension.moduleRootsFromIncludes(): Unit =
    project.pluginManager.withPlugin("org.jetbrains.kotlinx.knit") {
        moduleRoots =
            (project.settings.gradle.rootProject.subprojects.map { project ->
                project.path.replace(":", "/")
            } + ".")
    }

public val Project.knit: KnitPluginExtension get() = the()

public fun Project.knit(configure: KnitPluginExtension.() -> Unit): Unit = extensions.configure(configure)

