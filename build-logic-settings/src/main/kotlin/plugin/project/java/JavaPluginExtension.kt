package plugin.project.java

import gradle.java
import gradle.projectProperties
import org.gradle.api.Project

internal fun Project.configureJavaExtension() =
    pluginManager.withPlugin("java") {
       projectProperties.jvm.let { jvm ->
            java {
                jvm.applyTo(this)
            }
        }
    }
