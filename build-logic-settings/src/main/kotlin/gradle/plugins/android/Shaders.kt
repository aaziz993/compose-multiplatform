@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.android

import com.android.build.api.dsl.Shaders
import klib.data.type.act
import gradle.collection.tryAddAll
import gradle.collection.trySet
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
        receiver.glslcArgs tryAddAll glslcArgs
        receiver.glslcArgs trySet setGlslcArgs

        scopedGlslcArgs?.forEach { key, options ->
            receiver.glslcScopedArgs(key, *options.toTypedArray())
        }

        setScopedGlslcArgs?.act(receiver.scopedGlslcArgs::clear)?.forEach(receiver.scopedGlslcArgs::putAll)
    }
}
