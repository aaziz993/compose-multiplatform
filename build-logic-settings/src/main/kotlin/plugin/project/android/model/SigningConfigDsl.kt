package plugin.project.android.model

import com.android.build.api.dsl.SigningConfig
import gradle.android
import gradle.trySet
import org.gradle.api.Project
import plugin.project.gradle.model.Named

/**
 * DSL object for configuring options related to signing for APKs and bundles.
 *
 * [DefaultSigningConfig] extends this with options relating to just APKs
 *
 */
internal interface SigningConfigDsl : Named {

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
    override fun applyTo(named: org.gradle.api.Named) {
        named as SigningConfig

        named::storeFile trySet storeFile?.let(::file)
        named::storePassword trySet storePassword
        named::keyAlias trySet keyAlias
        named::keyPassword trySet keyPassword
        named::storeType trySet storeType
        initWith?.let(android.signingConfigs::getByName)?.let(named::initWith)
    }
}
