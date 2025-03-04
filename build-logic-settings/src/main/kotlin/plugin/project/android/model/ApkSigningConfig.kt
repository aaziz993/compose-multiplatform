package plugin.project.android.model

import com.android.build.api.dsl.ApkSigningConfig
import com.android.builder.signing.DefaultSigningConfig
import gradle.android
import gradle.trySet
import org.gradle.api.Project
import plugin.project.kotlin.model.Named

/** DSL object to configure signing configs. */
internal interface ApkSigningConfig : SigningConfigDsl, Named {

    /**
     * Enable signing using JAR Signature Scheme (aka v1 signing). If null, a default value is used.
     *
     * See [Signing Your Applications](http://developer.android.com/tools/publishing/app-signing.html)
     */
    val enableV1Signing: Boolean?

    /**
     * Enable signing using APK Signature Scheme v2 (aka v2 signing). If null, a default value is
     * used.
     *
     * See [Signing Your Applications](http://developer.android.com/tools/publishing/app-signing.html)
     */
    val enableV2Signing: Boolean?

    /**
     * Enable signing using APK Signature Scheme v3 (aka v3 signing). If null, a default value is
     * used.
     *
     * See [APK Signature Scheme v3](https://source.android.com/security/apksigning/v3)
     */
    val enableV3Signing: Boolean?

    /**
     * Enable signing using APK Signature Scheme v4 (aka v4 signing). If null, a default value is
     * used.
     */
    val enableV4Signing: Boolean?

    context(Project)
    fun toApkSigningConfig(): ApkSigningConfig =
        android.signingConfigs.create(name) {

            super<SigningConfigDsl>.applyTo(this)

            ::enableV1Signing trySet enableV1Signing
            ::enableV2Signing trySet enableV2Signing
            ::enableV3Signing trySet enableV3Signing
            ::enableV4Signing trySet enableV4Signing
        }
}
