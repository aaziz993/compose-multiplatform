package plugin.project.java

import gradle.javaApp
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin

internal fun Project.configureJavaApplication() =
    pluginManager.withPlugin(ApplicationPlugin.APPLICATION_PLUGIN_NAME) {
       projectProperties.jvm.application?.let { application ->
            javaApp(application::applyTo)
        }
    }
