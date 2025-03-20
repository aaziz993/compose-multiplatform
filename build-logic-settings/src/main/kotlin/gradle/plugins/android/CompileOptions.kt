package gradle.plugins.android

import com.android.build.api.dsl.CompileOptions
import gradle.accessors.libs
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.api.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.JavaVersion
import org.gradle.api.Project

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

    context(Project)
    fun applyTo(recipient: CompileOptions) {
        (sourceCompatibility ?: settings.libs.versions
            .version("java.sourceCompatibility")
            ?.let(JavaVersion::toVersion))
            ?.let(recipient::sourceCompatibility)
        (targetCompatibility ?: settings.libs.versions
            .version("java.targetCompatibility")
            ?.let(JavaVersion::toVersion))
            ?.let(recipient::targetCompatibility)

        recipient::encoding trySet encoding
        recipient::isCoreLibraryDesugaringEnabled trySet isCoreLibraryDesugaringEnabled
    }
}
