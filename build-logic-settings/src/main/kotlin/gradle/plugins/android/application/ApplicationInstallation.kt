package gradle.plugins.android.application

import com.android.build.api.dsl.ApplicationInstallation
import gradle.plugins.android.Installation
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable

@Serializable
internal data class ApplicationInstallation(
    override val timeOutInMs: Int? = null,
    override val installOptions: List<String>? = null,
    override val setInstallOptions: List<String>? = null,
    /** Whether to generate per-SDK level baseline profiles to install with an APK. */
    val enableBaselineProfile: Boolean? = null,
) : Installation<ApplicationInstallation> {

    @Suppress("UnstableApiUsage")
    override fun applyTo(receiver: ApplicationInstallation) {
        super.applyTo(receiver)

        receiver::enableBaselineProfile trySet enableBaselineProfile
    }
}
