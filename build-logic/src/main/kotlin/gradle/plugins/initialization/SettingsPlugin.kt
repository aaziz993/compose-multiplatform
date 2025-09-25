@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.initialization

import gradle.api.initialization.SettingsScript
import gradle.api.initialization.enableCacheRedirect
import gradle.api.initialization.settingsScript
import gradle.api.project.ProjectScript
import gradle.plugins.project.ProjectPlugin
import java.sql.DriverManager
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.sqlite.JDBC

public class SettingsPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        // Register H2 driver to avoid "No suitable driver" error. H2 is used by Gradle to store the build cache.
        DriverManager.registerDriver(JDBC())

        with(SLF4JProblemReporterContext()) {
            with(target) {
                // Load and apply settings.yaml.
                SettingsScript()

                enableCacheRedirect()

                (listOfNotNull(
                    settingsScript.licenseFile,
                    settingsScript.licenseHeaderFile,
                    settingsScript.codeOfConductFile,
                    settingsScript.contributingFile,
                ) + settingsScript.files)
                    .forEach { projectFile -> projectFile.sync() }

                gradle.projectsLoaded {
                    // At this point all projects have been created by settings.gradle.kts, but none were evaluated yet.
                    // Load project.yaml.
                    allprojects {
                        ProjectScript()
                    }

                    allprojects {
                        pluginManager.apply(ProjectPlugin::class.java)
                    }
                }
            }
        }
    }
}
