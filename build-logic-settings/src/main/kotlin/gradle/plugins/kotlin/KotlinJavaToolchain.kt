package gradle.plugins.kotlin

import gradle.accessors.javaToolchain
import gradle.plugins.java.JavaToolchainSpec
import kotlinx.serialization.Serializable
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinJavaToolchain

/**
 * The Kotlin JVM toolchain.
 *
 * This interface provides ways to configure the JDK either via [JdkSetter] by providing a path to JDK directly, or
 * via [JavaToolchainSetter] using the configured [JavaLauncher].
 *
 * The configured JDK Java version is exposed as a task input so that Gradle only reuses the task outputs stored in the
 * [build cache](https://docs.gradle.org/current/userguide/build_cache.html) with the same JDK version.
 */
@Serializable
internal data class KotlinJavaToolchain(
    /**
     * Provides access to the [JdkSetter] to configure the JVM toolchain for the task using an explicit JDK location.
     */
    val jdk: JdkSetter? = null,
    /**
     * Provides access to the [JavaToolchainSetter] to configure JVM toolchain for the task
     * using the [Gradle JVM toolchain](https://docs.gradle.org/current/userguide/toolchains.html).
     */
    val toolchain: JavaToolchainSpec? = null,
) {

    context(Project)
    fun applyTo(receiver: KotlinJavaToolchain) {
        jdk?.applyTo(receiver.jdk)

        toolchain?.let { javaLauncher ->
            receiver.toolchain.use(
                project.javaToolchain.launcherFor {
                    javaLauncher.applyTo(this)
                },
            )
        }
    }

    /**
     * Provides methods to configure the task using an explicit JDK location.
     */
    @Serializable
    data class JdkSetter(
        /**
         * Configures the JVM toolchain to use the JDK located under the [jdkHomeLocation] absolute path.
         * The major JDK version from [javaVersion] is used as a task input so that Gradle avoids using task outputs
         * in the [build cache](https://docs.gradle.org/current/userguide/build_cache.html) that use different JDK versions.
         *
         * **Note**: The project build fails if the JRE version instead of the JDK version is provided.
         *
         * @param jdkHomeLocation The path to the JDK location on the machine
         * @param jdkVersion The JDK version located in the configured [jdkHomeLocation] path
         */

        val use: Use? = null,
    ) {

        context(Project)
        fun applyTo(receiver: KotlinJavaToolchain.JdkSetter) {
            use?.applyTo(receiver)
        }

        @Serializable
        data class Use(
            val jdkHomeLocation: String,
            val jdkVersion: JavaVersion,
        ) {

            context(Project)
            fun applyTo(receiver: KotlinJavaToolchain.JdkSetter) {
                receiver.use(project.file(jdkHomeLocation), jdkVersion)
            }
        }
    }
}
