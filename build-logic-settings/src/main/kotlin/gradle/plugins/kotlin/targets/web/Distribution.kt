package gradle.plugins.kotlin.targets.web

import org.gradle.kotlin.dsl.assign
import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.Distribution

@Serializable
internal data class Distribution(
    val distributionName: String? = null,
    val outputDirectory: String? = null,
) {

    context(Project)
    fun applyTo(distribution: Distribution, distributionName: String) {
        distribution.distributionName = this.distributionName ?: distributionName
        distribution.outputDirectory tryAssign outputDirectory?.let(project.layout.projectDirectory::dir)
    }
}
