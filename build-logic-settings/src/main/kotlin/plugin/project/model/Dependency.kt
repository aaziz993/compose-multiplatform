package plugin.project.model

import gradle.all
import gradle.kotlin
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.model.dependency.DependencyNotation

@Serializable
internal data class Dependency(
    val sourceSetName: String,
    val dependencyNotations: List<DependencyNotation>,
) {

    context(Project)
    internal fun add(): Any = kotlin.sourceSets.matching { sourceSet -> sourceSet.name == sourceSetName }.all { sourceSet ->
        sourceSet.dependencies {
            dependencyNotations.forEach { dependencyNotation -> dependencyNotation.addTo(this) }
        }
    }
}
