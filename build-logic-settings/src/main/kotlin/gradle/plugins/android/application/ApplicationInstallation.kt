package gradle.plugins.android.application

import com.android.build.api.dsl.ApplicationInstallation
import gradle.plugins.android.Installation
import gradle.trySet
import kotlinx.serialization.Serializable

@Serializable
internal data class ApplicationInstallation(
    override var timeOutInMs: Int? = null,
    override val installOptions: List<String>? = null,
    /** Whether to generate per-SDK level baseline profiles to install with an APK. */
    val enableBaselineProfile: Boolean? = null,
) : Installation {

    @Suppress("UnstableApiUsage")
    override fun applyTo(installation: com.android.build.api.dsl.Installation) {
        super.applyTo(installation)

        installation as ApplicationInstallation

        installation::enableBaselineProfile trySet enableBaselineProfile
    }
}
