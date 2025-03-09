package gradle.model.kotlin.kmp.jvm.android.library

import com.android.build.api.dsl.LibraryBuildFeatures
import gradle.model.kotlin.kmp.jvm.android.BuildFeatures
import gradle.trySet
import kotlinx.serialization.Serializable

/**
 * A list of build features that can be disabled or enabled in an Android Library project.
 */
@Serializable
internal data class LibraryBuildFeatures(
    override val compose: Boolean? = null,
    override val buildConfig: Boolean? = null,
    override val prefab: Boolean? = null,
    override val renderScript: Boolean? = null,
    override val resValues: Boolean? = null,
    override val shaders: Boolean? = null,
    override val viewBinding: Boolean? = null,
    override val aidl: Boolean? = null,
    /**
     * Flag to disable Android resource processing.
     *
     * Setting the value to 'null' resets to the default value.
     * Default value is 'true'.
     *
     * You can override the default for this for all projects in your build by adding the line
     *     `android.library.defaults.buildfeatures.androidresources=false`
     * in the gradle.properties file at the root project of your build.
     *
     * Once set to 'false', flag disables
     * [com.android.build.api.dsl.LibraryBuildFeatures.dataBinding],
     * [com.android.build.api.dsl.BuildFeatures.viewBinding],
     * [com.android.build.api.dsl.BuildFeatures.renderScript].
     *
     * More information about this feature at: TBD
     */
    val androidResources: Boolean? = null,
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
    /**
     * Flag to enable generating Prefab packages for AARs.
     *
     * Setting the value to `null` resets to the default value.
     * Default value is `false`.
     *
     * You can override the default for this for all projects in your build by adding the line
     *     `android.defaults.buildfeatures.prefabPublishing=true`
     * in the `gradle.properties` file at the root project of your build.
     *
     * More information about this feature at: https://developer.android.com/studio/build/native-dependencies
     */
    val prefabPublishing: Boolean? = null,
) : BuildFeatures {

    override fun applyTo(features: com.android.build.api.dsl.BuildFeatures) {
        super.applyTo(features)

        features as LibraryBuildFeatures

        features::androidResources trySet androidResources
        features::dataBinding trySet dataBinding
        features::mlModelBinding trySet mlModelBinding
        features::prefabPublishing trySet prefabPublishing
    }
}
