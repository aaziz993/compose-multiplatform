package plugin.project.android.model

/**
 * A Signing Configuration.
 *
 * This is an interface for the gradle tooling api, and should only be used from Android Studio.
 * It is not part of the DSL & API interfaces of the Android Gradle Plugin.
 */
internal interface SigningConfig {
    /** Returns the name of the Signing config */
    val name: String

    /** The keystore file. */
    val storeFile: String?

    /** The keystore password. */
    val storePassword: String?

    /** The key alias name. */
    val keyAlias: String?

    /** The key password. */
    val keyPassword: String?

    /** The store type. */
    val storeType: String?

    /**
     * Whether the config is fully configured for signing.
     *
     * i.e. all the required information are present.
     */
    val isSigningReady: Boolean
}
