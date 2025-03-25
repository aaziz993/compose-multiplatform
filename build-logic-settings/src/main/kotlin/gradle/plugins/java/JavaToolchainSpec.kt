package gradle.plugins.java

import gradle.accessors.libs
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainSpec

/**
 * Requirements for selecting a Java toolchain.
 *
 *
 * A toolchain is a JRE/JDK used by the tasks of a build.
 * Tasks may require one or more of the tools (javac, java, or javadoc) of a toolchain.
 * Depending on the needs of a build, only toolchains matching specific characteristics can be used to run a build or a specific task of a build.
 *
 *
 * Even though specification properties can be configured independently,
 * the configuration must follow certain rules in order to form a  specification.
 *
 *
 * A `JavaToolchainSpec` is considered *valid* in two cases:
 *
 *  *  when no properties have been set, i.e. the specification is *empty*;
 *  *  when [language version][.getLanguageVersion] has been set, optionally followed by setting any other property.
 *
 *
 *
 * In other words, if a vendor or an implementation are specified, they must be accompanied by the language version.
 * An empty specification in most cases corresponds to the toolchain that runs the current build.
 *
 *
 * Usage of *invalid* instances of `JavaToolchainSpec` is deprecated and will be removed in the future versions of Gradle.
 *
 * @since 6.7
 */
@Serializable
internal data class JavaToolchainSpec(
    /**
     * The exact version of the Java language that the toolchain is required to support.
     */
    val languageVersion: JavaLanguageVersion? = null,

    /**
     * The vendor of the toolchain.
     *
     *
     * By default, toolchains from any vendor are eligible.
     *
     *
     * Note that the vendor can only be configured if the [language version][.getLanguageVersion] is configured as well.
     *
     * @since 6.8
     */
    val vendor: JvmVendorSpec? = null,
) {

    context(Project)
    fun applyTo(receiver: JavaToolchainSpec) {
        receiver.languageVersion tryAssign (languageVersion ?: project.settings.libs.versions.version("java.languageVersion")
            ?.let(JavaLanguageVersion::of))
        receiver.vendor tryAssign vendor?.toVendor()
    }
}
