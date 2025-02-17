package plugin.project.gradle.dokka

import gradle.amperModuleExtraProperties
import gradle.dokka
import gradle.maybeNamed
import gradle.tryAssign
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.dokka.gradle.internal.InternalDokkaGradlePluginApi
import plugin.project.gradle.dokka.model.DokkaExternalDocumentationLinkSpec
import plugin.project.gradle.dokka.model.DokkaPublication
import plugin.project.gradle.dokka.model.DokkaSourceSetSpec

@OptIn(InternalDokkaGradlePluginApi::class)
internal fun Project.configureDokkaExtension() =
    plugins.withType<DokkaPlugin> {
        amperModuleExtraProperties.settings.gradle.dokka.let { dokka ->
            dokka {
                basePublicationsDirectory tryAssign dokka.basePublicationsDirectory?.let(layout.projectDirectory::dir)
                dokkaCacheDirectory tryAssign dokka.dokkaCacheDirectory?.let(layout.projectDirectory::dir)
                moduleName tryAssign dokka.moduleName
                moduleVersion tryAssign dokka.moduleVersion
                modulePath tryAssign dokka.modulePath
                sourceSetScopeDefault tryAssign dokka.sourceSetScopeDefault
                konanHome tryAssign dokka.konanHome?.let(::file)

                dokka.dokkaPublications?.forEach { dokkaPublication ->
                    dokkaPublication.formatName?.also { formatName ->
                        dokkaPublications.maybeNamed(formatName) {
                            configureFrom(dokkaPublication)
                        }
                    } ?: dokkaPublications.configureEach {
                        configureFrom(dokkaPublication)
                    }
                }

                dokka.dokkaSourceSets?.forEach { dokkaSourceSet ->
                    dokkaSourceSet.name?.also { name ->
                        dokkaSourceSets.maybeNamed(name) {
                            configureFrom(dokkaSourceSet)
                        }
                    } ?: dokkaSourceSets.configureEach {
                        configureFrom(dokkaSourceSet)
                    }
                }

                dokkaEngineVersion tryAssign dokka.dokkaEngineVersion
            }
        }
    }

context(Project)
private fun org.jetbrains.dokka.gradle.formats.DokkaPublication.configureFrom(config: DokkaPublication) = apply {
    pluginsConfiguration
    enabled tryAssign config.enabled
    moduleName tryAssign config.moduleName
    moduleVersion tryAssign config.moduleVersion
    outputDirectory tryAssign config.outputDirectory?.let(layout.projectDirectory::dir)
    offlineMode tryAssign config.offlineMode
    failOnWarning tryAssign config.failOnWarning
    suppressObviousFunctions tryAssign config.suppressObviousFunctions
    suppressInheritedMembers tryAssign config.suppressInheritedMembers
    config.includes?.forEach(includes::setFrom)
    cacheRoot tryAssign config.cacheRoot?.let(layout.projectDirectory::dir)
    finalizeCoroutines tryAssign config.finalizeCoroutines
}

context(Project)
private fun org.jetbrains.dokka.gradle.engine.parameters.DokkaSourceSetSpec.configureFrom(config: DokkaSourceSetSpec) = apply {
    sourceSetScope tryAssign config.sourceSetScope
    suppress tryAssign config.suppress
    displayName tryAssign config.displayName
    config.includes?.forEach(includes::setFrom)
    documentedVisibilities tryAssign config.documentedVisibilities
    config.classpath?.forEach(classpath::setFrom)
    config.sourceRoots?.forEach(sourceRoots::setFrom)
    config.samples?.forEach(samples::setFrom)
    reportUndocumented tryAssign config.reportUndocumented

    config.sourceLinks?.forEach { sourceLink ->
        sourceLink {
            localDirectory tryAssign sourceLink.localDirectory?.let(layout.projectDirectory::dir)
            sourceLink.remoteUrl?.let(::remoteUrl)
            remoteLineSuffix tryAssign sourceLink.remoteLineSuffix
        }
    }

    config.perPackageOptions?.forEach { perPackageOption ->
        perPackageOption {
            matchingRegex tryAssign perPackageOption.matchingRegex
            suppress tryAssign perPackageOption.suppress
            documentedVisibilities tryAssign perPackageOption.documentedVisibilities
            skipDeprecated tryAssign perPackageOption.skipDeprecated
            reportUndocumented tryAssign perPackageOption.reportUndocumented
        }
    }

    config.externalDocumentationLinks?.forEach { externalDocumentationLink ->
        externalDocumentationLink.name?.also { name ->
            externalDocumentationLinks.maybeNamed(name) {
                configureFrom(externalDocumentationLink)
            }
        } ?: externalDocumentationLinks.configureEach {
            configureFrom(externalDocumentationLink)
        }
    }

    analysisPlatform tryAssign config.analysisPlatform
    skipEmptyPackages tryAssign config.skipEmptyPackages
    skipDeprecated tryAssign config.skipDeprecated
    config.suppressedFiles?.forEach(suppressedFiles::setFrom)
    suppressGeneratedFiles tryAssign config.suppressGeneratedFiles
    enableKotlinStdLibDocumentationLink tryAssign config.enableKotlinStdLibDocumentationLink
    enableJdkDocumentationLink tryAssign config.enableJdkDocumentationLink
    enableAndroidDocumentationLink tryAssign config.enableAndroidDocumentationLink
    languageVersion tryAssign config.languageVersion
    apiVersion tryAssign config.apiVersion
    jdkVersion tryAssign config.jdkVersion
}

private fun org.jetbrains.dokka.gradle.engine.parameters.DokkaExternalDocumentationLinkSpec.configureFrom(
    config: DokkaExternalDocumentationLinkSpec
) = apply {
    config.url?.let(::url)
    config.packageListUrl?.let(::packageListUrl)
    enabled tryAssign config.enabled
}







