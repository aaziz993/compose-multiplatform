package plugins

import gradle.plugins.project.ProjectProperties
import gradle.problemreporter.SLF4JProblemReporterContext
import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.yaml.snakeyaml.Yaml

private const val PROJECT_PROPERTIES_FILE = "project.yaml"

/**
 * Gradle setting plugin, that is responsible for:
 * 1. Initializing gradle projects, based on the Amper model.
 * 2. Applying kotlin or KMP plugins.
 * 3. Associate gradle projects with modules.
 */
// This is registered via FQN from the resources in org.jetbrains.amper.settings.plugin.properties
public class SettingsPlugin : Plugin<Settings> {

    private val json = Json { ignoreUnknownKeys = true }

    private val yaml = Yaml()

    @Suppress("UnstableApiUsage")
    override fun apply(target: Settings) {
        with(SLF4JProblemReporterContext()) {
            with(target) {
                // Load and apply settings.gradle.kts properties from project.yaml.
                ProjectProperties.applyTo()
            }

            target.gradle.projectsLoaded {
                // at this point all projects have been created by settings.gradle.kts, but none were evaluated yet
                allprojects {
                    // Load and apply build.gradle.kts properties from project.yaml.
                    ProjectProperties.applyTo()
                }
            }
        }
    }
}
