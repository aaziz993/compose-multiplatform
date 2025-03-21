@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugins.initialization

import gradle.accessors.allLibs
import gradle.accessors.exportExtras
import gradle.accessors.libs
import gradle.accessors.projectProperties
import gradle.accessors.toVersionCatalogUrlPath
import gradle.accessors.version
import gradle.accessors.versions
import gradle.api.configureEach
import gradle.api.repositories.CacheRedirector
import gradle.isUrl
import gradle.project.ProjectProperties.Companion.load
import java.net.URI
import org.gradle.api.Plugin
import org.gradle.api.file.FileCollection
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.artifacts.repositories.DefaultMavenArtifactRepository
import org.jetbrains.compose.internal.IDEA_IMPORT_TASK_NAME
import org.jetbrains.compose.internal.utils.currentTarget
import org.tomlj.Toml
import plugins.develocity.DevelocityPlugin
import plugins.githooks.GitHooksPlugin
import plugins.initialization.problemreporter.SLF4JProblemReporterContext
import plugins.project.ProjectPlugin
import plugins.toolchainmanagement.ToolchainManagementPlugin

private const val VERSION_CATALOG_CACHE_DIR = "build-logic-settings/gradle"

private const val COMPOSE_VERSION_CATALOG_FILE = "build-logic-settings/gradle/compose.versions.template.toml"

/**
 * Gradle setting plugin, that is responsible for:
 * 1. Initializing gradle projects, based on the Amper model.
 * 2. Applying kotlin or KMP plugins.
 * 3. Associate gradle projects with modules.
 */
public class SettingsPlugin : Plugin<Settings> {

    @Suppress("UnstableApiUsage")
    override fun apply(target: Settings) {
        with(SLF4JProblemReporterContext()) {
            with(target) {
                // Load and apply project.yaml to settings.gradle.kts.
                projectProperties = layout.settingsDirectory.load(layout.settingsDirectory)

                exportExtras()

                projectProperties.buildscript?.applyTo()

                projectProperties.pluginManagement.let { pluginManagement ->
                    pluginManagement {
                        pluginManagement?.repositories?.toTypedArray()?.let(importTask::dependsOn)
                    }
                }
            }
        }
    }
}
