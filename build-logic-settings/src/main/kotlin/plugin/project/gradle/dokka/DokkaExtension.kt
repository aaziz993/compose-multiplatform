package plugin.project.gradle.dokka

import gradle.dokka
import gradle.maybeNamed
import gradle.moduleProperties
import gradle.tryAssign
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.dokka.gradle.internal.InternalDokkaGradlePluginApi

@OptIn(InternalDokkaGradlePluginApi::class)
internal fun Project.configureDokkaExtension() =
    plugins.withType<DokkaPlugin> {
        moduleProperties.settings.gradle.dokka.let { dokka ->
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
                            dokkaPublication.applyTo(this)
                        }
                    } ?: dokkaPublications.configureEach {
                        dokkaPublication.applyTo(this)
                    }
                }

                dokka.dokkaSourceSets?.forEach { dokkaSourceSet ->
                    dokkaSourceSet.name?.also { name ->
                        dokkaSourceSets.maybeNamed(name) {
                            dokkaSourceSet.applyTo(this)
                        }
                    } ?: dokkaSourceSets.configureEach {
                        dokkaSourceSet.applyTo(this)
                    }
                }

                dokkaEngineVersion tryAssign dokka.dokkaEngineVersion
            }
        }
    }






