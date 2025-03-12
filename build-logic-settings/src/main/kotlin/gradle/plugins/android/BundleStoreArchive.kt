package gradle.plugins.android

import com.android.build.api.dsl.BundleStoreArchive
import gradle.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring the App Bundle Store Archive options
 *
 * This is accessed via [Bundle.storeArchive]
 */
@Serializable
internal data class BundleStoreArchive(
    /**
     * Archive is an app state that allows an official app store to reclaim device storage and
     * disable app functionality temporarily until the user interacts with the app again. Upon
     * interaction the latest available version of the app will be restored while leaving user data
     * unaffected.
     *
     * <p> Enabled by default.
     */
    val enable: Boolean? = null,
) {

    fun applyTo(archive: BundleStoreArchive) {
        archive::enable trySet enable
    }
}
