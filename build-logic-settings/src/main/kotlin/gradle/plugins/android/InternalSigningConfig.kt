package gradle.plugins.android

import com.android.build.gradle.internal.dsl.InternalSigningConfig
import gradle.Named
import org.gradle.api.Project

/** Serves the same purpose as [InternalBuildType] */
internal interface InternalSigningConfig :
    SigningConfig,
    ApkSigningConfig,
    Named,
    SigningConfigDsl {

    context(Project)
    override fun applyTo(named: org.gradle.api.Named) {
        super<ApkSigningConfig>.applyTo(named)

        named as InternalSigningConfig

        super<SigningConfigDsl>.applyTo(named)
    }
}
