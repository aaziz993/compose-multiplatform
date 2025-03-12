package gradle.plugins.android.library

import com.android.build.api.dsl.LibraryVariantDimension
import gradle.plugins.android.AarMetadata
import gradle.plugins.android.ApkSigningConfigImpl
import gradle.plugins.android.VariantDimension
import gradle.api.trySet
import org.gradle.api.Project

/**
 * Shared properties between DSL objects that contribute to a library variant.
 *
 * That is, [LibraryBuildType] and [LibraryProductFlavor] and [LibraryDefaultConfig].
 */
internal interface LibraryVariantDimension : VariantDimension {

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
    val signingConfig: ApkSigningConfigImpl?

    /** Options for configuring AAR metadata. */
    val aarMetadata: AarMetadata?

    context(Project)
    override fun applyTo(dimension: com.android.build.api.dsl.VariantDimension) {
        super.applyTo(dimension)

        dimension as LibraryVariantDimension

        dimension::multiDexEnabled trySet multiDexEnabled

        consumerProguardFiles?.let { consumerProguardFiles ->
            dimension.consumerProguardFiles(*consumerProguardFiles.toTypedArray())
        }

        dimension::signingConfig trySet signingConfig?.toApkSigningConfig()
        aarMetadata?.applyTo(dimension.aarMetadata)
    }
}
