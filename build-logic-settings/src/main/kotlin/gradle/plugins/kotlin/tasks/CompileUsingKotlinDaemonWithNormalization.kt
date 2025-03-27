@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.kotlin.tasks

import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilerExecutionStrategy

internal interface CompileUsingKotlinDaemonWithNormalization<T : org.jetbrains.kotlin.gradle.tasks.CompileUsingKotlinDaemonWithNormalization>
    : CompileUsingKotlinDaemon<T> {

}

@Serializable
@SerialName("CompileUsingKotlinDaemonWithNormalization")
internal data class CompileUsingKotlinDaemonWithNormalizationImpl(
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
) : CompileUsingKotlinDaemonWithNormalization<org.jetbrains.kotlin.gradle.tasks.CompileUsingKotlinDaemonWithNormalization> {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.CompileUsingKotlinDaemonWithNormalization>())
}
