package gradle.plugins.android.signing

import com.android.build.gradle.internal.dsl.InternalSigningConfig
import gradle.api.ProjectNamed
import org.gradle.api.Project

/** Serves the same purpose as [InternalBuildType] */
internal interface InternalSigningConfig<T : InternalSigningConfig> :
    SigningConfig,
    ApkSigningConfig<T>,
    ProjectNamed<T>,
    SigningConfigDsl<T> {

    context(Project)
    override fun applyTo(recipient: T) {
        super<ApkSigningConfig>.applyTo(recipient)

        super<SigningConfigDsl>.applyTo(recipient)
    }
}
