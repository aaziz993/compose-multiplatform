package plugin.project.java

import gradle.java
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureJavaExtension() =
    pluginManager.withPlugin("java") {
       settings.projectProperties.jvm.let { jvm ->
            java {
                jvm.applyTo(this)
            }
        }
    }
