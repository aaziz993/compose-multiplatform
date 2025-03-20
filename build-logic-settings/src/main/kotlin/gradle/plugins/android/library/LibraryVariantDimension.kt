package gradle.plugins.android.library

import com.android.build.api.dsl.LibraryVariantDimension
import gradle.accessors.android
import gradle.api.trySet
import gradle.plugins.android.AarMetadata
import gradle.plugins.android.ApkSigningConfigImpl
import gradle.plugins.android.VariantDimension
import org.gradle.api.Project

/**
 * Shared properties between DSL objects that contribute to a library variant.
 *
 * That is, [LibraryBuildType] and [LibraryProductFlavor] and [LibraryDefaultConfig].
 */
internal interface LibraryVariantDimension<in T : LibraryVariantDimension> : VariantDimension<T> {

    /**
     * Returns whether multi-dex is enabled.
     *
     * This can be null if the flag is not set, in which case the default value is used.
     */
    val multiDexEnabled: Boolean?

    /**
     * ProGuard rule files to be included in the published AAR.
     *
     * These proguard rule files will then be used by any application project that consumes the
     * AAR (if ProGuard is enabled).
     *
     * This allows AAR to specify shrinking or obfuscation exclude rules.
     *
     * This is only valid for Library project. This is ignored in Application project.
     */
    val consumerProguardFiles: List<String>?

    /** The associated signing config or null if none are set on the variant dimension. */
    val signingConfig: String?

    /** Options for configuring AAR metadata. */
    val aarMetadata: AarMetadata?

    context(Project)
    override fun applyTo(recipient: T) {
        super.applyTo(dimension)

        dimension::multiDexEnabled trySet multiDexEnabled

        consumerProguardFiles?.let { consumerProguardFiles ->
            dimension.consumerProguardFiles(*consumerProguardFiles.toTypedArray())
        }

        dimension::signingConfig trySet signingConfig?.let(android.signingConfigs::getByName)
        aarMetadata?.applyTo(dimension.aarMetadata)
    }
}
