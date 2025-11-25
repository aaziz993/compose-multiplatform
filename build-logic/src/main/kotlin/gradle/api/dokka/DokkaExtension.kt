package gradle.api.dokka

import gradle.api.artifacts.dsl.dokka
import gradle.api.project.settings
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.dokka.gradle.DokkaExtension
import gradle.api.artifacts.dsl.project

// Dokka multimodule module dependencies. By default taken from includes see dokka.dependenciesFromIncludes.
@Suppress("UnusedReceiverParameter")
context(project: Project)
public fun DokkaExtension.dependenciesFromIncludes(predicate: (Project) -> Boolean = { true }): Unit =
    project.pluginManager.withPlugin("org.jetbrains.dokka") {
        project.settings.gradle.rootProject.subprojects
            .filter(predicate)
            .map(project.dependencies::project)
            .forEach(project.dependencies::dokka)
    }

public val Project.dokka: DokkaExtension get() = the()

public fun Project.dokka(configure: DokkaExtension.() -> Unit): Unit = extensions.configure(configure)
