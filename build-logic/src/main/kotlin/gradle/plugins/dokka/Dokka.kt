package gradle.plugins.dokka

import gradle.api.artifacts.dsl.dokka
import gradle.api.project.moduleName
import gradle.api.project.settings
import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.engine.plugins.DokkaHtmlPluginParameters
import org.jetbrains.dokka.gradle.engine.plugins.DokkaPluginParametersBaseSpec
import org.jetbrains.dokka.gradle.engine.plugins.DokkaVersioningPluginParameters

public val ExtensiblePolymorphicDomainObjectContainer<DokkaPluginParametersBaseSpec>.html: DokkaHtmlPluginParameters
    get() = getByName(DokkaHtmlPluginParameters.DOKKA_HTML_PARAMETERS_NAME) as DokkaHtmlPluginParameters

public val ExtensiblePolymorphicDomainObjectContainer<DokkaPluginParametersBaseSpec>.versioning: DokkaVersioningPluginParameters
    get() = getByName(DokkaVersioningPluginParameters.DOKKA_VERSIONING_PLUGIN_PARAMETERS_NAME) as DokkaVersioningPluginParameters

// dokka multimodule module dependencies. By default taken from includes see dokka.dependenciesFromIncludes.
context(project: Project)
public fun DokkaExtension.dependenciesFromIncludes(): Unit =
    project.pluginManager.withPlugin("org.jetbrains.dokka") {
        project.settings.gradle.rootProject.subprojects
            .map(Project::getPath)
            .forEach(project.dependencies::dokka)
    }
