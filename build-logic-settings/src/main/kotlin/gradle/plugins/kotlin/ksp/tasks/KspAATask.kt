package gradle.plugins.kotlin.ksp.tasks

import com.google.devtools.ksp.gradle.KspAATask
import gradle.api.file.tryFrom
import gradle.api.file.trySetFrom
import gradle.api.provider.tryAddAll
import gradle.api.provider.tryAssign
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import klib.data.type.serialization.json.serializer.SerializableAnyMap
import gradle.plugins.kotlin.ksp.KspGradleConfig
import gradle.process.CommandLineArgumentProvider
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class KspAATask(
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
    val kspClasspath: Set<String>? = null,
    val setKspClasspath: Set<String>? = null,
    val kspConfig: KspGradleConfig? = null,
    val commandLineArgumentProviders: List<CommandLineArgumentProvider>? = null,
    val setCommandLineArgumentProviders: List<CommandLineArgumentProvider>? = null,
) : DefaultTask<KspAATask>() {

    context(Project)
    override fun applyTo(receiver: KspAATask) {
        super.applyTo(receiver)

        receiver.kspClasspath tryFrom kspClasspath
        receiver.kspClasspath trySetFrom setKspClasspath
        kspConfig?.applyTo(receiver.kspConfig)
        receiver.commandLineArgumentProviders tryAddAll commandLineArgumentProviders
        receiver.commandLineArgumentProviders tryAssign setCommandLineArgumentProviders
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<KspAATask>())
}
