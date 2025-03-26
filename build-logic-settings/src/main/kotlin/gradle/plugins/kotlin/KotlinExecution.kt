package gradle.plugins.kotlin

import gradle.api.BaseNamed
import gradle.api.ProjectNamed
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinExecution

/**
 * Represents the execution of Kotlin code, such as tests.
 *
 * Executions within a single family, like test runs, are distinguished by [BaseNamed.getName].
 * Names don't have to be globally unique across different execution families.
 * For example, test runs of different targets can have the same name.
 *
 * [KotlinTestRun] is a specific type of execution that runs tests.
 */
internal interface KotlinExecution<T : KotlinExecution<E>, E : KotlinExecution.ExecutionSource> : ProjectNamed<T> {

    /**
     * Represents an execution source that provides the necessary inputs to run the execution.
     */
    interface ExecutionSource

    /**
     * The source of the executable code that this execution runs.
     *
     * It is typically set via members of [ExecutionSource] support interfaces,
     * such as [CompilationExecutionSourceSupport] or [ClasspathTestRunSourceSupport].
     */
    val executionSource: ExecutionSource?

    context(Project)
    override fun applyTo(receiver: T)
}
