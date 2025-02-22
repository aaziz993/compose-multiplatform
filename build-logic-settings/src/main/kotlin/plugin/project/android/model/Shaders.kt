package plugin.project.android.model

import com.android.build.api.dsl.Shaders
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.com.google.common.collect.ListMultimap

/**
 * Options for configuring scoped shader options.
 */
@Serializable
internal data class Shaders(
    /**
     * The list of glslc args.
     */
    val glslcArgs: List<String>? = null,

    /**
     * The list of scoped glsl args.
     */
    val scopedGlslcArgs: Map<String, List<String>>? = null,
) {

    fun applyTo(shaders: Shaders) {
        glslcArgs?.let(shaders.glslcArgs::addAll)
        shaders.glslcScopedArgs()
        scopedGlslcArgs?.forEach { key, options ->
            shaders.glslcScopedArgs(key, *options.toTypedArray())
        }
    }
}
