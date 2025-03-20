package gradle.plugins.android.application

import com.android.build.api.dsl.ApplicationBuildFeatures
import gradle.api.trySet
import gradle.plugins.android.BuildFeatures
import kotlinx.serialization.Serializable

/**
 * A list of build features that can be disabled or enabled in an Android Application project.
 */
@Serializable
internal data class ApplicationBuildFeatures(
    override val aidl: Boolean? = null,
    override val compose: Boolean? = null,
    override val buildConfig: Boolean? = null,
    override val prefab: Boolean? = null,
    override val renderScript: Boolean? = null,
    override val resValues: Boolean? = null,
    override val shaders: Boolean? = null,
    override val viewBinding: Boolean? = null,
    /**
     * Flag to enable Data Binding.
     *
     * Setting the value to `null` resets to the default value.
     * Default value is `false`.
     *
     * You can override the default for this for all projects in your build by adding the line
     *     `android.defaults.buildfeatures.databinding=true`
     * in the `gradle.properties` file at the root project of your build.
     *
     * More information about this feature at: TBD
     */
    val dataBinding: Boolean? = null,
    /**
     * Flag to enable Machine Learning Model Binding.
     *
     * Setting the value to `null` resets to the default value.
     * Default value is `false`.
     *
     * You can override the default for this for all projects in your build by adding the line
     *     `android.defaults.buildfeatures.mlmodelbinding=true`
     * in the `gradle.properties` file at the root project of your build.
     *
     * More information about this feature at: TBD
     */
    val mlModelBinding: Boolean? = null,
) : BuildFeatures {

    override fun applyTo(recipient: com.android.build.api.dsl.BuildFeatures) {
        super.applyTo(features)

        features as ApplicationBuildFeatures

        features::dataBinding trySet dataBinding
        features::mlModelBinding trySet mlModelBinding
    }
}
