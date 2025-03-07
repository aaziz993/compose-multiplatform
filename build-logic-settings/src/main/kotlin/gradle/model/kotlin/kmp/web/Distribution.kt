package gradle.model.kotlin.kmp.web

import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.Distribution

@Serializable
internal data class Distribution(
    val distributionName: String? = null,
    val outputDirectory: String? = null,
) {

    context(Project)
    fun applyTo(distribution: Distribution) {
        distribution.distributionName tryAssign distributionName
        distribution.outputDirectory tryAssign outputDirectory?.let(layout.projectDirectory::dir)
    }
}
