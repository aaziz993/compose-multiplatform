@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.initialization

import gradle.api.initialization.SettingsScript
import gradle.api.initialization.settingsScript
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
                // Load and apply settings.yaml to settings.gradle.kts.
                SettingsScript()

                (listOfNotNull(
                    target.settingsScript.licenseFile,
                    target.settingsScript.licenseHeaderFile,
                    target.settingsScript.codeOfConductFile,
                    target.settingsScript.contributingFile,
                ) + settingsScript.files)
                    .forEach { projectFile -> projectFile.sync() }

                gradle.projectsLoaded {
                    // At this point all projects have been created by settings.gradle.kts, but none were evaluated yet.
                    allprojects {
                        pluginManager.apply(ProjectPlugin::class.java)
                    }
                }
            }
        }
    }
}
