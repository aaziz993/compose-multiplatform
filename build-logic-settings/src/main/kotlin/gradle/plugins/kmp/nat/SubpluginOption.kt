package gradle.plugins.kmp.nat

import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

@Serializable
internal data class SubpluginOption(
    val key: String,
    val lazyValue: String
) {

    fun toSubpluginOption() = SubpluginOption(key, lazy { lazyValue })
}
