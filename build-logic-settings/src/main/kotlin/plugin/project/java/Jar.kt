package plugin.project.java

import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.attributes
import org.gradle.kotlin.dsl.withType

internal fun Project.configureJar() {
    tasks.withType<Jar> {

    }
}
