package plugin.project.android.model

import org.gradle.api.Project
import plugin.project.gradle.model.Named

/** Serves the same purpose as [InternalBuildType] */
internal interface InternalSigningConfig :
    SigningConfig,
    ApkSigningConfig,
    Named,
    SigningConfigDsl {

    context(Project)
    override fun applyTo(named: org.gradle.api.Named) {
        super<ApkSigningConfig>.applyTo(named)
        super<SigningConfigDsl>.applyTo(named)
    }
}
