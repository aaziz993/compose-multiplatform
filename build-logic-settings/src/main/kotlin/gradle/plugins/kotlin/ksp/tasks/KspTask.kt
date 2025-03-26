package gradle.plugins.kotlin.ksp.tasks

import gradle.api.tasks.Task
import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.SubpluginOption
import gradle.process.CommandLineArgumentProvider
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal interface KspTask<T : com.google.devtools.ksp.gradle.KspTask> : Task<T> {

    val options: List<SubpluginOption>?
    val setOptions: List<SubpluginOption>?

    val commandLineArgumentProviders: List<CommandLineArgumentProvider>?
    val setCommandLineArgumentProviders: List<CommandLineArgumentProvider>?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver.options tryAssign options?.map(SubpluginOption::toSubpluginOption)?.let { options ->
            receiver.options.get() + options
        }

        receiver.options tryAssign setOptions?.map(SubpluginOption::toSubpluginOption)

        receiver.commandLineArgumentProviders tryAssign commandLineArgumentProviders?.let { options ->
            receiver.commandLineArgumentProviders.get() + options
        }

        receiver.commandLineArgumentProviders tryAssign setCommandLineArgumentProviders
    }
}

@Serializable
@SerialName("KspTask")
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
        applyTo(project.tasks.withType<com.google.devtools.ksp.gradle.KspTask>())
}
