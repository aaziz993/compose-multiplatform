package gradle.plugins.kotlin.ksp.tasks

import gradle.api.tasks.Task
import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.SubpluginOption
import gradle.process.CommandLineArgumentProvider
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType


internal interface KspTask<T : com.google.devtools.ksp.gradle.KspTask> : Task<T> {

    val options: List<SubpluginOption>?
    val setOptions: List<SubpluginOption>?

    val commandLineArgumentProviders: List<CommandLineArgumentProvider>?
    val setCommandLineArgumentProviders: List<CommandLineArgumentProvider>?

    context(Project)
    override fun applyTo(recipient: T) {
        super.applyTo(recipient)

        recipient.options tryAssign options?.map(SubpluginOption::toSubpluginOption)?.let { options ->
            recipient.options.get() + options
        }

        recipient.options tryAssign setOptions?.map(SubpluginOption::toSubpluginOption)

        recipient.commandLineArgumentProviders tryAssign commandLineArgumentProviders?.let { options ->
            recipient.commandLineArgumentProviders.get() + options
        }

        recipient.commandLineArgumentProviders tryAssign setCommandLineArgumentProviders
    }
}

@Serializable
internal data class KspTaskImpl(
    override val options: List<SubpluginOption>? = null,
    override val setOptions: List<SubpluginOption>? = null,
    override val commandLineArgumentProviders: List<CommandLineArgumentProvider>? = null,
    override val setCommandLineArgumentProviders: List<CommandLineArgumentProvider>? = null,
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
) : KspTask<com.google.devtools.ksp.gradle.KspTask> {

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<com.google.devtools.ksp.gradle.KspTask>())
}
