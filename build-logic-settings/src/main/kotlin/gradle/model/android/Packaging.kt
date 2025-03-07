package gradle.model.android

import com.android.build.gradle.internal.dsl.PackagingOptions
import kotlinx.serialization.Serializable

/**
 * Options for APK packaging.
 */
@Serializable
internal data class Packaging(
    /** Packaging options for dex files */
    val dex: DexPackaging? = null,
    /** Packaging options for JNI library files */
    val jniLibs: JniLibsPackaging? = null,
    /** Packaging options for java resources */
    val resources: ResourcesPackaging? = null
) {

    fun applyTo(options: PackagingOptions) {
        dex?.applyTo(options.dex)
        jniLibs?.applyTo(options.jniLibs)
        resources?.applyTo(options.resources)
    }
}
