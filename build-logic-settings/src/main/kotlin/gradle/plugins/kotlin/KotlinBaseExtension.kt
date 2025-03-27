package gradle.plugins.kotlin

import gradle.accessors.libs
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.api.tryAssign
import gradle.api.trySet
import gradle.plugins.java.JavaToolchainSpec
import org.gradle.api.Project
import org.jetbrains.kotlin.buildtools.api.ExperimentalBuildToolsApi
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension

internal interface KotlinBaseExtension<T : KotlinBaseExtension> : KotlinSourceSetContainer<T> {

    /**
     * Configures [Java toolchain](https://docs.gradle.org/current/userguide/toolchains.html)
     * both for Kotlin JVM and Java tasks in the project.
     *
     * @param action - action to configure [JavaToolchainSpec]
     */
    val jvmToolchainSpec: JavaToolchainSpec?

    /**
     * Configures [Java toolchain](https://docs.gradle.org/current/userguide/toolchains.html)
     * both for Kotlin JVM and Java tasks in the project.
     *
     * @param jdkVersion - JDK version as number. For example, 17 for Java 17.
     */
    val jvmToolchain: Int?

    /**
     * Configures Kotlin daemon JVM arguments for all tasks in this project.
     *
     * **Note**: In case other projects are using different JVM arguments,
     * a new instance of Kotlin daemon will be started.
     */
    val kotlinDaemonJvmArgs: List<String>?
    val setKotlinDaemonJvmArgs: List<String>?

    /**
     * The version of the Kotlin compiler.
     *
     * By default, the Kotlin Build Tools API implementation of the same version as the KGP is used.
     *
     * Be careful with reading the property's value as eager reading will finalize the value and prevent it from being configured.
     *
     * Note: Currently only has an effect if the `kotlin.compiler.runViaBuildToolsApi` Gradle property is set to `true`.
     */
    val compilerVersion: String?

    /**
     * Specifies the version of the core Kotlin libraries that are added to the Kotlin compile classpath,
     * unless there is already a dependency added to this project.
     *
     * The core Kotlin libraries are:
     * - 'kotlin-stdlib'
     * - 'kotlin-test'
     * - 'kotlin-dom-api-compat'
     * - 'kotlin-reflect'
     *
     * Default: The same version as the version used in the Kotlin Gradle plugin
     */
    val coreLibrariesVersion: String?

    /**
     * Configures default explicit API mode for all non-test compilations in the project.
     *
     * This mode tells the compiler if and how to report issues on all public API declarations
     * that don't have an explicit visibility or return type.
     *
     * Default: `null`
     */
    val explicitApi: ExplicitApiMode?

    context(Project)
    @OptIn(ExperimentalBuildToolsApi::class)
    fun applyTo(receiver: T) {
        jvmToolchainSpec?.let { jvmToolchainSpec ->
            receiver.jvmToolchain {
                jvmToolchainSpec.applyTo(this)
            }
        } ?: (jvmToolchain ?: project.settings.libs.versions.version("java.languageVersion")?.toInt())
            ?.let(receiver::jvmToolchain)

        receiver::kotlinDaemonJvmArgs trySet kotlinDaemonJvmArgs?.let { kotlinDaemonJvmArgs ->
            receiver.kotlinDaemonJvmArgs + kotlinDaemonJvmArgs
        }

        receiver::kotlinDaemonJvmArgs trySet setKotlinDaemonJvmArgs
        receiver.compilerVersion tryAssign (compilerVersion
            ?: project.settings.libs.versions.version("kotlin.compilerVersion"))
        receiver::coreLibrariesVersion trySet (coreLibrariesVersion
            ?: project.settings.libs.versions.version("kotlin.coreLibrariesVersion"))
        explicitApi?.let { explicitApi ->
            receiver.explicitApi = explicitApi
        }
    }
}
