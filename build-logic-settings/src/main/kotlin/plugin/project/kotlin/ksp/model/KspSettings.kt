package plugin.project.kotlin.ksp.model

import gradle.tryAssign
import gradle.trySet
import kotlinx.serialization.Serializable
import plugin.model.dependency.Dependency

@Serializable
internal data class KspSettings(
    override val useKsp2: Boolean? = null,
    override val commandLineArgumentProviders: List<String>? = null,
    override val excludedProcessors: List<String>? = null,
    override val excludedSources: List<String>? = null,
    override val arguments: Map<String, String>? = null,
    override val allWarningsAsErrors: Boolean? = null,
    val processors: List<Dependency>? = null,
    val enabled: Boolean = true,
) : KspExtension {

    fun applyTo(extension: com.google.devtools.ksp.gradle.KspExtension) {
        extension.useKsp2 tryAssign useKsp2

        commandLineArgumentProviders?.let { commandLineArgumentProviders ->
            extension.arg { commandLineArgumentProviders }
        }

        excludedProcessors?.forEach(extension::excludeProcessor)
        excludedSources?.let(extension.excludedSources::setFrom)
        arguments?.forEach { (key, value) -> extension.arg(key, value) }
        extension::allWarningsAsErrors trySet allWarningsAsErrors
    }
}
