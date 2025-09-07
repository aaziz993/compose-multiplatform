@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.initialization

import gradle.api.configureEach
import gradle.api.initialization.SettingsProperties
import gradle.api.initialization.file.LicenseHeaderFile
import gradle.api.initialization.settingsProperties
import gradle.plugins.project.ProjectPlugin
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.jetbrains.compose.internal.IDEA_IMPORT_TASK_NAME
import org.sqlite.JDBC
import java.sql.DriverManager

public class SettingsPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        // Register H2 driver to avoid "No suitable driver" error. H2 is used by Gradle to store the build cache.
        DriverManager.registerDriver(JDBC())

        with(SLF4JProblemReporterContext()) {
            with(target) {
                // Load and apply settings.yaml to settings.gradle.kts.
                SettingsProperties()

                gradle.projectsLoaded {
                    // Apply project files
                    with(rootProject) {
                        val projectFiles = (listOfNotNull(
                            target.settingsProperties.licenseFile,
                            target.settingsProperties.codeOfConductFile,
                            target.settingsProperties.contributingFile,
                        ) + LicenseHeaderFile("licenses/LICENSE_HEADER") + settingsProperties.files)
                            .flatMapIndexed { index, projectFile -> projectFile.applyTo("projectFile$index") }

                        //setup sync tasks execution during IDE import
                        tasks.configureEach { importTask ->
                            if (importTask.name == IDEA_IMPORT_TASK_NAME) {
                                importTask.dependsOn(*projectFiles.toTypedArray())
                            }
                        }
                    }
                    // at this point all projects have been created by settings.gradle.kts, but none were evaluated yet
                    allprojects {
                        pluginManager.apply(ProjectPlugin::class.java)
                    }
                }
            }
        }
    }
}
