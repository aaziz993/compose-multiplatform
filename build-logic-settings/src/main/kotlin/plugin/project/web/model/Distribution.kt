package plugin.project.web.model

import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.targets.js.dsl.Distribution

@Serializable
internal data class Distribution(
    val distributionName: String? = null,
    val outputDirectory: String? = null,
){
    fun applyTo(distribution: Distribution){
        distribution.distributionName tryAssign distributionName
        distribution.outputDirectory tryAssign outputDirectory
    }
}
