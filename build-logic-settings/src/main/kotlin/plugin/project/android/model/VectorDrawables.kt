package plugin.project.android.model

import com.android.build.api.dsl.VectorDrawables
import gradle.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object used to configure `vector` drawable options.
 */
@Serializable
internal data class VectorDrawables(
    /**
     * Densities used when generating PNGs from vector drawables at build time. For the PNGs to be
     * generated, minimum SDK has to be below 21.
     *
     * If set to an empty collection, all special handling of vector drawables will be
     * disabled.
     *
     * See
     * [Supporting Multiple Screens](http://developer.android.com/guide/practices/screens_support.html).
     */
    val generatedDensities: Set<String>? = null,
    /**
     * Whether to use runtime support for `vector` drawables, instead of build-time support.
     *
     * See [Vector Asset Studio](http://developer.android.com/tools/help/vector-asset-studio.html).
     */
    var useSupportLibrary: Boolean? = null,
) {

    @Suppress("UnstableApiUsage")
    fun applyTo(drawables: VectorDrawables) {
        generatedDensities?.let { generatedDensities ->
            drawables.generatedDensities?.addAll(generatedDensities)
        }

        drawables::useSupportLibrary trySet useSupportLibrary
    }
}
