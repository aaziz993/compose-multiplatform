package gradle.plugins.android.test

import com.android.build.api.dsl.AndroidTest
import gradle.api.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object on library build type for configurations of the android test component.
 *
 * android {
 *     buildTypes {
 *         debug {
 *             androidTest {
 *                 enableMinification = ...
 *             }
 *         }
 *     }
 * }
 *
 */
@Serializable
internal data class AndroidTest(
    /** Enable minification for the android test component */
    val enableMinification: Boolean? = null,
) {

    @Suppress("UnstableApiUsage")
    fun applyTo(recipient: AndroidTest) {
        recipient::enableMinification trySet enableMinification
    }
}
