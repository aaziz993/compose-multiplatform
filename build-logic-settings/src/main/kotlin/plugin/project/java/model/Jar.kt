package plugin.project.java.model

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.attributes

@Serializable
internal data class Jar(
    val manifest: Manifest? = null,
) {

    context(Project)
    fun applyTo(jar: Jar) {
        manifest?.let { manifest ->
            jar.manifest {
                attributes(
                    "Implementation-Title" to name,
                    "Implementation-Version" to version,
                    "Automatic-Module-Name" to name,
                )
                manifest.attributes?.let(attributes::putAll)
            }
        }
    }
}
