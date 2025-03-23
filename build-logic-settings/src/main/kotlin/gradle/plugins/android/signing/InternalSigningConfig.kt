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
    override fun applyTo(receiver: T) {
        super<ApkSigningConfig>.applyTo(receiver)

        super<SigningConfigDsl>.applyTo(receiver)
    }
}
