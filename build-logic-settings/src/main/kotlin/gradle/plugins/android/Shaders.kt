@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.android

import com.android.build.api.dsl.Shaders
import gradle.collection.act
import kotlinx.serialization.Serializable

/**
 * Options for configuring scoped shader options.
 */
@Serializable
internal data class Shaders(
    /**
     * The list of glslc args.
     */
    val glslcArgs: List<String>? = null,
    val setGlslcArgs: List<String>? = null,

    /**
     * The list of scoped glsl args.
     */
    val scopedGlslcArgs: Map<String, List<String>>? = null,
    val setScopedGlslcArgs: Map<String, List<String>>? = null,
) {

    fun applyTo(receiver: Shaders) {
        glslcArgs?.let(receiver.glslcArgs::addAll)
        setGlslcArgs?.act(receiver.glslcArgs::clear)?.let(receiver.glslcArgs::addAll)

        scopedGlslcArgs?.forEach { key, options ->
            receiver.glslcScopedArgs(key, *options.toTypedArray())
        }

//        setScopedGlslcArgs?.act(receiver.scopedGlslcArgs::clear)?.let(receiver.scopedGlslcArgs::addAll) // TODO
    }
}
