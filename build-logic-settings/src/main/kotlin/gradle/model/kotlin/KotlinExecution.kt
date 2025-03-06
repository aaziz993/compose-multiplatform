package gradle.model.kotlin

import gradle.model.Named

/**
 * Represents the execution of Kotlin code, such as tests.
 *
 * Executions within a single family, like test runs, are distinguished by [Named.getName].
 * Names don't have to be globally unique across different execution families.
 * For example, test runs of different targets can have the same name.
 *
 * [KotlinTestRun] is a specific type of execution that runs tests.
 */
internal interface KotlinExecution<out SourceType : KotlinExecution.ExecutionSource> : Named {

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
    val executionSource: SourceType?
}
