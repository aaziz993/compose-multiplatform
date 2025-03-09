package gradle.model.kotlin.kmp.jvm.android

import com.android.build.api.dsl.SigningConfig
import gradle.android
import gradle.projectProperties
import gradle.resolveSensitive
import gradle.trySet
import net.pearx.kasechange.toDotCase
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
        signingConfig::storePassword trySet storePassword?.resolveSensitive()
        signingConfig::keyAlias trySet keyAlias?.resolveSensitive()
        signingConfig::keyPassword trySet keyPassword?.resolveSensitive()
        signingConfig::storeType trySet storeType?.resolveSensitive()
        initWith?.let(android.signingConfigs::getByName)?.let(signingConfig::initWith)
    }
}
