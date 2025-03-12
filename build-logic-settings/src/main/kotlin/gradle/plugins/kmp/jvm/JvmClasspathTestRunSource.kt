package gradle.plugins.kmp.jvm

import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.KotlinExecution

/**
 * A [KotlinExecution.ExecutionSource] that provides the [classpath] and [testClassesDirs] where JVM test classes can be found.
 */
@Serializable
internal data class JvmClasspathTestRunSource(

    /**
     * The part of the classpath where JVM test classes are located for execution.
     */
    val testClassesDirs: List<String>,

    /**
     * The tests classpath.
     *
     * This includes dependencies and/or test framework classes such as JUnit, TestNG, or any other test framework classes.
     */
    val classpath: List<String>
)
