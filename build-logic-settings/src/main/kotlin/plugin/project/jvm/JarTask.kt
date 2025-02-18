package plugin.project.jvm

import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.attributes
import org.gradle.kotlin.dsl.withType

internal fun Project.configureJar() {
    tasks.withType<Jar>().configureEach {
        manifest {
            attributes(
                "Implementation-Title" to (project.description.takeIf { !it.isNullOrEmpty() } ?: project.name),
                "Implementation-Version" to project.version,
                "Automatic-Module-Name" to project.name,
            )
        }
    }
}
