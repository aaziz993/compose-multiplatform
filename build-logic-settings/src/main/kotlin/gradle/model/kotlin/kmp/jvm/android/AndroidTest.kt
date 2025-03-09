package gradle.model.kotlin.kmp.jvm.android

import com.android.build.api.dsl.AndroidTest
import gradle.trySet
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
    fun applyTo(test: AndroidTest) {
        test::enableMinification trySet enableMinification
    }
}
