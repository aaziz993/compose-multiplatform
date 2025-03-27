package gradle.plugins.kotlin.targets.nat

import gradle.plugins.kotlin.SubpluginOption
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.tasks.CompilerPluginOptions

@Serializable
internal data class CompilerPluginOptions(
    override val optionsByPluginId: Map<String, List<SubpluginOption>>? = null
) : CompilerPluginConfig() {

    fun toCompilerPluginOptions() =
        CompilerPluginOptions(super.toCompilerPluginConfig())
}
