package gradle.plugins.android

import com.android.build.api.dsl.SigningConfig
import gradle.accessors.android
import gradle.accessors.resolveValue
import gradle.api.trySet
import org.gradle.api.Project

/**
 * DSL object for configuring options related to signing for APKs and bundles.
 *
 * [DefaultSigningConfig] extends this with options relating to just APKs
 *
 */
internal interface SigningConfigDsl<in T: SigningConfig> {

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
    fun applyTo(recipient: T) {
        recipient::storeFile trySet storeFile?.let(::file)
        recipient::storePassword trySet storePassword?.resolveValue()?.toString()
        recipient::keyAlias trySet keyAlias?.resolveValue()?.toString()
        recipient::keyPassword trySet keyPassword?.resolveValue()?.toString()
        recipient::storeType trySet storeType?.resolveValue()?.toString()
        initWith?.let(android.signingConfigs::getByName)?.let(recipient::initWith)
    }
}
