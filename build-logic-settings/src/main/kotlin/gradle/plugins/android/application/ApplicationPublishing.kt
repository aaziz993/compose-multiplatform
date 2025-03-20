package gradle.plugins.android.application

import com.android.build.api.dsl.ApplicationPublishing
import gradle.plugins.android.Publishing
import kotlinx.serialization.Serializable

/**
 * Maven publishing DSL object for configuring options related to publishing APK and AAB.
 *
 * This following code example creates a publication for the fullRelease build variant, which
 * publish your app as Android App Bundle.
 *
 * ```
 * android {
 *     // This project has four build variants: fullDebug, fullRelease, demoDebug, demoRelease
 *     flavorDimensions 'mode'
 *     productFlavors {
 *         full {}
 *         demo {}
 *     }
 *
 *     publishing {
 *         // Publish your app as an AAB
 *         singleVariant("fullRelease")
 *     }
 * }
 *
 * afterEvaluate {
 *     publishing {
 *         publications {
 *             fullRelease(MavenPublication) {
 *                 from components.fullRelease
 *                 // ......
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * To publish your app as a ZIP file of APKs, simply use the [ApplicationSingleVariant.publishApk]
 * as shown in the following example.
 *
 * ```
 * android {
 *     publishing {
 *         // Publish your app as a ZIP file of APKs
 *         singleVariant("fullRelease") {
 *             publishApk()
 *         }
 *     }
 * }
 * ```
 */
@Serializable
internal data class ApplicationPublishing(
    override val singleVariants: List<ApplicationSingleVariant>? = null,
) : Publishing<ApplicationPublishing, com.android.build.api.dsl.ApplicationSingleVariant, ApplicationSingleVariant>
