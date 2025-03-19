package gradle.plugins.android

import com.android.build.gradle.internal.dsl.InternalSigningConfig
import gradle.api.BaseNamed

/** Serves the same purpose as [InternalBuildType] */
internal interface InternalSigningConfig :
    SigningConfig,
    ApkSigningConfig,
    BaseNamed,
    SigningConfigDsl {

        context(Project)
    override fun applyTo(named: T) {
        super<ApkSigningConfig>.applyTo(named)

        named as InternalSigningConfig

        super<SigningConfigDsl>.applyTo(named)
    }
}
