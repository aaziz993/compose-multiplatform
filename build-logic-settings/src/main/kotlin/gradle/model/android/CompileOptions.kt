package gradle.model.android

import com.android.build.api.dsl.CompileOptions
import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.JavaVersion

/**
 * Java compilation options.
 */
@Serializable
internal data class CompileOptions(
    /**
     * Language level of the java source code.
     *
     * Similar to what [Gradle Java plugin](http://www.gradle.org/docs/current/userguide/java_plugin.html)
     * uses. Formats supported are:
     *
     * - `"1.6"`
     * - `1.6`
     * - `JavaVersion.Version_1_6`
     * - `"Version_1_6"`
     */
    val sourceCompatibility: JavaVersion? = null,
    /**
     * Version of the generated Java bytecode.
     *
     * Similar to what [Gradle Java plugin](http://www.gradle.org/docs/current/userguide/java_plugin.html)
     * uses. Formats supported are:
     *
     * - `"1.6"`
     * - `1.6`
     * - `JavaVersion.Version_1_6`
     * - `"Version_1_6"`
     */
    val targetCompatibility: JavaVersion? = null,
    /** Java source files encoding. */
    val encoding: String? = null,
    /** Whether core library desugaring is enabled. */
    val isCoreLibraryDesugaringEnabled: Boolean? = null
) {

    fun applyTo(options: CompileOptions) {
        sourceCompatibility?.let(options::sourceCompatibility)
        targetCompatibility?.let(options::targetCompatibility)
        options::encoding trySet encoding
        options::isCoreLibraryDesugaringEnabled trySet isCoreLibraryDesugaringEnabled
    }
}
