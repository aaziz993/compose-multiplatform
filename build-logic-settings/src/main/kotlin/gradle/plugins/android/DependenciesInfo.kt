package gradle.plugins.android

import com.android.build.api.dsl.DependenciesInfo
import gradle.api.trySet
import kotlinx.serialization.Serializable

/** DSL object to specify whether to include SDK dependency information in APKs and Bundles.
 *
 * Including dependency information in your APK or Bundle allows Google Play to ensure that
 * any third-party software your app uses complies with
 * [Google Play's Developer Program Policies](https://support.google.com/googleplay/android-developer/topic/9858052).
 * For more information, see the Play Console support page
 * [Using third-party SDKs in your app](https://support.google.com/googleplay/android-developer/answer/10358880).*/
@Serializable
internal data class DependenciesInfo(
    /** If false, information about SDK dependencies of an APK will not be added to its signature
     * block. */
    var includeInApk: Boolean? = null,
    /** If false, information about SDK dependencies of an App Bundle will not be added to it. */
    var includeInBundle: Boolean? = null,
) {

    fun applyTo(recipient: DependenciesInfo) {
        recipient::includeInApk trySet includeInApk
        recipient::includeInBundle trySet includeInBundle
    }
}
