package gradle.plugins.dokka

import gradle.api.artifacts.dsl.dokka
import gradle.api.project.settings
import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.engine.plugins.DokkaHtmlPluginParameters
import org.jetbrains.dokka.gradle.engine.plugins.DokkaPluginParametersBaseSpec
import org.jetbrains.dokka.gradle.engine.plugins.DokkaVersioningPluginParameters

// Dokka multimodule module dependencies. By default taken from includes see dokka.dependenciesFromIncludes.
@Suppress("UnusedReceiverParameter")
context(project: Project)
public fun DokkaExtension.dependenciesFromIncludes(): Unit =
    project.pluginManager.withPlugin("org.jetbrains.dokka") {
        project.settings.gradle.rootProject.subprojects
            .map(Project::getPath)
            .forEach(project.dependencies::dokka)
    }
