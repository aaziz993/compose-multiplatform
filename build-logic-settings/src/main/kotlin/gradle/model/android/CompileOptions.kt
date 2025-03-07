package gradle.model.android

import com.android.build.api.dsl.CompileOptions
import gradle.libs
import gradle.settings
import gradle.trySet
import gradle.version
import gradle.versions
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
    fun applyTo(options: CompileOptions) {
        (sourceCompatibility ?: settings.libs.versions
            .version("java.sourceCompatibility")
            ?.let(JavaVersion::toVersion))
            ?.let(options::sourceCompatibility)
        (targetCompatibility ?: settings.libs.versions
            .version("java.targetCompatibility")
            ?.let(JavaVersion::toVersion))
            ?.let(options::targetCompatibility)

        options::encoding trySet encoding
        options::isCoreLibraryDesugaringEnabled trySet isCoreLibraryDesugaringEnabled
    }
}
