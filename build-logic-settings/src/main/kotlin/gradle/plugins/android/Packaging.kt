package gradle.plugins.android

import com.android.build.api.dsl.Packaging
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

    fun applyTo(recipient: Packaging) {
        dex?.applyTo(recipient.dex)
        jniLibs?.applyTo(recipient.jniLibs)
        resources?.applyTo(recipient.resources)
    }
}
