package gradle.plugins.android

import com.android.build.api.dsl.ApkSigningConfig
import gradle.accessors.android
import gradle.api.BaseNamed
import gradle.api.ProjectNamed
import gradle.api.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/** DSL object to configure signing configs. */
internal interface ApkSigningConfig<T : ApkSigningConfig> : SigningConfigDsl<T>, ProjectNamed<T> {

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
    override fun applyTo(recipient: T) {
        super<SigningConfigDsl>.applyTo(named)

        named::enableV1Signing trySet enableV1Signing
        named::enableV2Signing trySet enableV2Signing
        named::enableV3Signing trySet enableV3Signing
        named::enableV4Signing trySet enableV4Signing
    }
}


@Serializable
internal data class ApkSigningConfigImpl(
    override val enableV1Signing: Boolean? = null,
    override val enableV2Signing: Boolean? = null,
    override val enableV3Signing: Boolean? = null,
    override val enableV4Signing: Boolean? = null,
    override val storeFile: String? = null,
    override val storePassword: String? = null,
    override val keyAlias: String? = null,
    override val keyPassword: String? = null,
    override val storeType: String? = null,
    override val initWith: String? = null,
    override val name: String,
) : gradle.plugins.android.ApkSigningConfig<ApkSigningConfig>
