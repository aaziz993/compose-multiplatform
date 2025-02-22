package plugin.project.android.model

import com.android.build.api.dsl.ApkSigningConfig
import gradle.trySet
import org.gradle.api.Project

/** DSL object to configure signing configs. */
internal data class DefaultSigningConfig(
    override val storeFile: String? = null,
    override val storePassword: String? = null,
    override val keyAlias: String? = null,
    override val keyPassword: String? = null,
    override val storeType: String? = null,
    /**
     * Enable signing using JAR Signature Scheme (aka v1 signing). If null, a default value is used.
     *
     * See [Signing Your Applications](http://developer.android.com/tools/publishing/app-signing.html)
     */
    val enableV1Signing: Boolean? = null,
    /**
     * Enable signing using APK Signature Scheme v2 (aka v2 signing). If null, a default value is
     * used.
     *
     * See [Signing Your Applications](http://developer.android.com/tools/publishing/app-signing.html)
     */
    val enableV2Signing: Boolean? = null,
    /**
     * Enable signing using APK Signature Scheme v3 (aka v3 signing). If null, a default value is
     * used.
     *
     * See [APK Signature Scheme v3](https://source.android.com/security/apksigning/v3)
     */
    val enableV3Signing: Boolean? = null,
    /**
     * Enable signing using APK Signature Scheme v4 (aka v4 signing). If null, a default value is
     * used.
     */
    val enableV4Signing: Boolean? = null,
    val name: String
) : SigningConfig {

    context(Project)
    fun applyTo(config: ApkSigningConfig) {
        super.applyTo(config)
        config::enableV1Signing trySet enableV1Signing
        config::enableV2Signing trySet enableV2Signing
        config::enableV3Signing trySet enableV3Signing
        config::enableV4Signing trySet enableV4Signing
    }
}
