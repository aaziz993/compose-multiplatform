@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.initialization

import gradle.api.configureEach
import gradle.api.initialization.SettingsScript
import gradle.api.initialization.file.LicenseHeaderFile
import gradle.api.initialization.settingsScript
import gradle.plugins.project.ProjectPlugin
import java.sql.DriverManager
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.jetbrains.compose.internal.IDEA_IMPORT_TASK_NAME
import org.sqlite.JDBC

public class SettingsPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        // Register H2 driver to avoid "No suitable driver" error. H2 is used by Gradle to store the build cache.
        DriverManager.registerDriver(JDBC())

        with(SLF4JProblemReporterContext()) {
            with(target) {
                // Load and apply settings.yaml to settings.gradle.kts.
                SettingsScript()

                gradle.projectsLoaded {
                    // Apply project files
                    with(rootProject) {
                        val files = (listOfNotNull(
                            target.settingsScript.licenseFile,
                            target.settingsScript.codeOfConductFile,
                            target.settingsScript.contributingFile,
                        ) + LicenseHeaderFile("licenses/LICENSE_HEADER") + settingsScript.files)
                            .flatMapIndexed { index, projectFile -> projectFile.applyTo("projectFile$index") }

                        //setup sync tasks execution during IDE import
                        tasks.configureEach { importTask ->
                            if (importTask.name == IDEA_IMPORT_TASK_NAME) {
                                importTask.dependsOn(*files.toTypedArray())
                            }
                        }
                    }

                    // At this point all projects have been created by settings.gradle.kts, but none were evaluated yet.
                    allprojects {
                        pluginManager.apply(ProjectPlugin::class.java)
                    }
                }
            }
        }
    }
}
