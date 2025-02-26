package plugin.project.android.model

import com.android.build.api.dsl.BuildFeatures
import gradle.trySet
import kotlinx.serialization.Serializable

/**
 * A list of build features that can be disabled or enabled in an Android project.
 *
 * This list applies to all plugin types.
 */
@Serializable
internal data class BuildFeatures(
    /**
     * Flag to enable AIDL compilation.
     *
     * Setting the value to `null` resets to the default value.
     * Default value is `false`.
     *
     * See [AIDL](http://developer.android.com/r/tools/reference/dsl/buildfeatures/aidl).
     */
    val aidl: Boolean? = null,
    /**
     * Flag to enable Compose feature.
     * Setting the value to `null` resets to the default value
     *
     * Default value is `false`.
     *
     * See [Compose](http://developer.android.com/compose).
     **/
    val compose: Boolean? = null,
    /**
     * Flag to enable/disable generation of the `BuildConfig` class.
     *
     * Setting the value to `null` resets to the default value.
     * Default value is `false`.
     *
     * See [build config fields](http://developer.android.com/r/tools/build-config-fields).
     */
    val buildConfig: Boolean? = null,
    /**
     * Flag to enable/disable import of Prefab dependencies from AARs.
     *
     * Setting the value to `null` resets to the default value.
     * Default value is `false`.
     *
     * You can override the default for this in your module by setting
     *     android {
     *         buildFeatures {
     *             prefab true
     *         }
     *     }
     * in the module's build.gradle file.
     *
     * See [Prefab](http://developer.android.com/r/tools/prefab).
     */
    val prefab: Boolean? = null,
    /**
     * Flag to enable RenderScript compilation.
     *
     * Setting the value to `null` resets to the default value.
     * Default value is `false`.
     *
     */
    val renderScript: Boolean? = null,
    /**
     * Flag to enable Resource Values generation.
     *
     * Setting the value to `null` resets to the default value.
     * Default value is `true`.
     *
     * You can override the default for this for all projects in your build by adding the line
     *     `android.defaults.buildfeatures.resvalues=true`
     * in the gradle.properties file at the root project of your build.

     * See [Resources](http://developer.android.com/r/tools/res-values).
     */
    val resValues: Boolean? = null,
    /**
     * Flag to enable Shader compilation.
     *
     * Setting the value to `null` resets to the default value.
     * Default value is `true`.
     *
     * You can override the default for this for all projects in your build by adding the line
     *     `android.defaults.buildfeatures.shaders=true`
     * in the gradle.properties file at the root project of your build.
     *
     * See [Shader Compilers](https://developer.android.com/r/tools/shader-compilers)
     */
    val shaders: Boolean? = null,
    /**
     * Flag to enable View Binding.
     *
     * Setting the value to `null` resets to the default value.
     * Default value is `false`.
     *
     * You can override the default for this for all projects in your build by adding the line
     *     `android.defaults.buildfeatures.viewbinding=true`
     * in the gradle.properties file at the root project of your build.
     *
     * See [View Binding Library](https://developer.android.com/viewbinding)
     */
    val viewBinding: Boolean? = null,
) {

    fun applyTo(features: BuildFeatures) {
        features::aidl trySet aidl
        features::compose trySet compose
        features::buildConfig trySet buildConfig
        features::prefab trySet prefab
        features::renderScript trySet renderScript
        features::resValues trySet resValues
        features::shaders trySet shaders
        features::viewBinding trySet viewBinding
    }
}
