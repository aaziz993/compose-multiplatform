package gradle.plugins.kmp.nat

import gradle.plugins.kotlin.SubpluginOption
import org.jetbrains.kotlin.gradle.plugin.CompilerPluginConfig

/**
 * Represents a container containing all the settings for a specific Kotlin compiler plugin.
 *
 * This container is available for all Kotlin compilation tasks as a [org.jetbrains.kotlin.gradle.tasks.BaseKotlinCompile.pluginOptions] input.
 */
internal abstract class CompilerPluginConfig {

    abstract val optionsByPluginId: Map<String, List<SubpluginOption>>?

    fun applyTo(recipient: CompilerPluginConfig) {
        optionsByPluginId?.forEach { (pluginId, options) ->
            options.forEach { option ->
                config.addPluginArgument(pluginId, option.toSubpluginOption())
            }
        }
    }

    fun toCompilerPluginConfig() = CompilerPluginConfig().apply(::applyTo)
}
