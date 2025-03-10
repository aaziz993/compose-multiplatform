package gradle.model.android

import com.android.build.api.dsl.SigningConfig
import gradle.android
import gradle.resolveValue
import gradle.trySet
import org.gradle.api.Project

/**
 * DSL object for configuring options related to signing for APKs and bundles.
 *
 * [DefaultSigningConfig] extends this with options relating to just APKs
 *
 */
internal interface SigningConfigDsl {

    /**
     * Store file used when signing.
     *
     * See [Signing Your Applications](http://developer.android.com/tools/publishing/app-signing.html)
     */
    val storeFile: String?

    /**
     * Store password used when signing.
     *
     * See [Signing Your Applications](http://developer.android.com/tools/publishing/app-signing.html)
     */
    val storePassword: String?

    /**
     * Key alias used when signing.
     *
     * See [Signing Your Applications](http://developer.android.com/tools/publishing/app-signing.html)
     */
    val keyAlias: String?

    /**
     * Key password used when signing.
     *
     * See [Signing Your Applications](http://developer.android.com/tools/publishing/app-signing.html)
     */
    val keyPassword: String?

    /**
     * Store type used when signing.
     *
     * See [Signing Your Applications](http://developer.android.com/tools/publishing/app-signing.html)
     */
    val storeType: String?

    /**
     * Copies all properties from the given signing config.
     */
    val initWith: String?

    context(Project)
    fun applyTo(signingConfig: SigningConfig) {
        signingConfig::storeFile trySet storeFile?.let(::file)
        signingConfig::storePassword trySet storePassword?.resolveValue()?.toString()
        signingConfig::keyAlias trySet keyAlias?.resolveValue()?.toString()
        signingConfig::keyPassword trySet keyPassword?.resolveValue()?.toString()
        signingConfig::storeType trySet storeType?.resolveValue()?.toString()
        initWith?.let(android.signingConfigs::getByName)?.let(signingConfig::initWith)
    }
}
