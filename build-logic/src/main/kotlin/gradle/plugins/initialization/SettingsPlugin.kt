package gradle.plugins.initialization

import gradle.api.initialization.SettingsProperties
import gradle.plugins.project.ProjectPlugin
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.h2.Driver
import java.sql.DriverManager

public class SettingsPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        // Register H2 driver to avoid "No suitable driver" error. H2 is used by Gradle to store the build cache.
        DriverManager.registerDriver(Driver())

        with(SLF4JProblemReporterContext()) {
            with(target) {
                // Load and apply settings.yaml to settings.gradle.kts.
                SettingsProperties()

                gradle.projectsLoaded {
                    // at this point all projects have been created by settings.gradle.kts, but none were evaluated yet
                    allprojects {
                        pluginManager.apply(ProjectPlugin::class.java)
                    }
                }
            }
        }
    }
}
