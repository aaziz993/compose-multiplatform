package plugin

import gradle.deepMerge
import gradle.projectProperties
import gradle.resolve
import gradle.serialization.decodeFromAny
import gradle.serialization.encodeToAny
import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.nodes.Tag
import org.yaml.snakeyaml.representer.Represent
import org.yaml.snakeyaml.representer.Representer
import plugin.project.model.ProjectProperties
import plugin.project.problemreporter.SLF4JProblemReporterContext

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
                // Setup  settings.gradle.kts from project.yaml.
                projectProperties = target.layout.settingsDirectory.loadProperties()
                projectProperties.applyTo()
            }

            target.gradle.projectsLoaded {
                // at this point all projects have been created by settings.gradle.kts, but none were evaluated yet
                allprojects {
                    // Load project properties.
                    projectProperties = layout.projectDirectory.loadProperties().apply {
                        println("Applied $PROJECT_PROPERTIES_FILE to: $name")
                        println(yaml.dump(Json.Default.encodeToAny(this)))
                    }

                    projectProperties.applyTo()
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun Directory.loadProperties(): ProjectProperties {
        val propertiesFile = file(PROJECT_PROPERTIES_FILE).asFile

        if (!propertiesFile.exists()) {
            return ProjectProperties()
        }

        val properties = yaml.load<MutableMap<String, *>>(propertiesFile.readText())

        val templatedProperties = (properties["templates"] as List<String>?)?.fold(emptyMap<String, Any?>()) { acc, template ->
            acc deepMerge yaml.load<MutableMap<String, *>>(file(template).asFile.readText())
        }.orEmpty() deepMerge properties

        return json.decodeFromAny<ProjectProperties>(templatedProperties.resolve())
    }
}
