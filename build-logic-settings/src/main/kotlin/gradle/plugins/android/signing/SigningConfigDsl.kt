package gradle.plugins.android.signing

import com.android.build.api.dsl.SigningConfig
import gradle.accessors.android
import gradle.reflect.trySet
import org.gradle.api.Project

/**
 * DSL object for configuring options related to signing for APKs and bundles.
 *
 * [DefaultSigningConfig] extends this with options relating to just APKs
 *
 */
internal interface SigningConfigDsl<T : SigningConfig> {

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
    fun applyTo(receiver: T) {
        receiver::storeFile trySet storeFile?.let(project::file)
        receiver::storePassword trySet storePassword
        receiver::keyAlias trySet keyAlias
        receiver::keyPassword trySet keyPassword
        receiver::storeType trySet storeType
        initWith?.let(project.android.signingConfigs::getByName)?.let(receiver::initWith)
    }
}
