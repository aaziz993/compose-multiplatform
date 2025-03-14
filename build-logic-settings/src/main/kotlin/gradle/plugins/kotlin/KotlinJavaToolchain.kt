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
internal interface KotlinJavaToolchain {

    /**
     * Provides access to the [JdkSetter] to configure the JVM toolchain for the task using an explicit JDK location.
     */
    val jdk: JdkSetter?

    /**
     * Provides access to the [JavaToolchainSetter] to configure JVM toolchain for the task
     * using the [Gradle JVM toolchain](https://docs.gradle.org/current/userguide/toolchains.html).
     */
    val toolchain: JavaToolchainSpec?

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
        val jdkHomeLocation: String,
        val jdkVersion: JavaVersion,
    ) {

        context(Project)
        fun applyTo(setter: KotlinJavaToolchain.JdkSetter) {
            setter.use(file(jdkHomeLocation), jdkVersion)
        }
    }

    context(Project)
    fun applyTo(toolchain: KotlinJavaToolchain) {
        jdk?.applyTo(toolchain.jdk)
        this@KotlinJavaToolchain.toolchain?.let { javaLauncher ->
            toolchain.toolchain.use(
                javaToolchain.launcherFor {
                    javaLauncher.applyTo(this)
                },
            )
        }
    }
}
