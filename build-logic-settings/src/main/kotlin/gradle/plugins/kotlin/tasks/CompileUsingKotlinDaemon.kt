package gradle.plugins.kotlin.tasks

import gradle.api.tasks.Task
import gradle.api.tasks.applyTo
import gradle.api.tryAddAll
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilerExecutionStrategy

/**
 * Represents a Kotlin task that uses the Kotlin daemon to compile.
 */
internal interface CompileUsingKotlinDaemon<T : org.jetbrains.kotlin.gradle.tasks.CompileUsingKotlinDaemon> : Task<T> {

    /**
     * Provides JVM arguments to the Kotlin daemon. The default is `null` if the `kotlin.daemon.jvmargs` property is not set.
     */
    val kotlinDaemonJvmArguments: List<String>?
    val setKotlinDaemonJvmArguments: List<String>?

    /**
     * Defines the compiler execution strategy, see docs for [KotlinCompilerExecutionStrategy] for more details.
     *
     * @see [KotlinCompilerExecutionStrategy]
     */
    val compilerExecutionStrategy: KotlinCompilerExecutionStrategy?

    /**
     * Defines whether task execution should fail when [compilerExecutionStrategy] is set to [KotlinCompilerExecutionStrategy.DAEMON]
     * and compilation via the Kotlin daemon is not possible. If set to true, then compilation is retried without the daemon.
     *
     * Default: `true`
     */
    val useDaemonFallbackStrategy: Boolean?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver.kotlinDaemonJvmArguments tryAddAll kotlinDaemonJvmArguments
        receiver.kotlinDaemonJvmArguments tryAssign setKotlinDaemonJvmArguments
        receiver.compilerExecutionStrategy tryAssign compilerExecutionStrategy
        receiver.useDaemonFallbackStrategy tryAssign useDaemonFallbackStrategy
    }
}

@Serializable
@SerialName("CompileUsingKotlinDaemon")
internal data class CompileUsingKotlinDaemonImpl(
    override val kotlinDaemonJvmArguments: List<String>? = null,
    override val setKotlinDaemonJvmArguments: List<String>? = null,
    override val compilerExecutionStrategy: KotlinCompilerExecutionStrategy? = null,
    override val useDaemonFallbackStrategy: Boolean? = null,
    override val dependsOn: LinkedHashSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: LinkedHashSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String? = null,
) : CompileUsingKotlinDaemon<org.jetbrains.kotlin.gradle.tasks.CompileUsingKotlinDaemon> {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.CompileUsingKotlinDaemon>())
}
