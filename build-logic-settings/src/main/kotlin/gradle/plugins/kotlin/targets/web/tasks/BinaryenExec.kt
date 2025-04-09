package gradle.plugins.kotlin.targets.web.tasks

import org.gradle.kotlin.dsl.withType
import gradle.api.provider.tryAssign
import gradle.api.tasks.applyTo
import gradle.process.AbstractExecTask
import klib.data.type.collection.tryAddAll
import klib.data.type.collection.trySet
import klib.data.type.serialization.json.serializer.SerializableAnyList
import klib.data.type.serialization.serializer.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal abstract class BinaryenExec<T : org.jetbrains.kotlin.gradle.targets.js.binaryen.BinaryenExec> :
    AbstractExecTask<T>() {

    abstract val binaryenArgs: List<String>?
    abstract val setBinaryenArgs: List<String>?

    abstract val inputFileProperty: String?

    abstract val outputDirectory: String?

    abstract val outputFileName: String?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver.binaryenArgs tryAddAll binaryenArgs
        receiver.binaryenArgs trySet setBinaryenArgs
        receiver.inputFileProperty tryAssign inputFileProperty?.let(project.layout.projectDirectory::file)
        receiver.outputDirectory tryAssign outputDirectory?.let(project.layout.projectDirectory::dir)
        receiver.outputFileName tryAssign outputFileName
    }
}

@Serializable
@SerialName("BinaryenExec")
internal data class BinaryenExecImpl(
    override val binaryenArgs: List<String>? = null,
    override val setBinaryenArgs: List<String>? = null,
    override val inputFileProperty: String? = null,
    override val outputDirectory: String? = null,
    override val outputFileName: String? = null,
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
    override val commandLineArgs: SerializableAnyList? = null,
    override val setCommandLineArgs: SerializableAnyList? = null,
    override val args: SerializableAnyList? = null,
    override val setArgs: SerializableAnyList? = null,
    override val ignoreExitValue: Boolean? = null,
    override val executable: String? = null,
    override val workingDir: String? = null,
    override val environment: SerializableAnyMap? = null,
    override val setEnvironment: SerializableAnyMap? = null,
) : BinaryenExec<org.jetbrains.kotlin.gradle.targets.js.binaryen.BinaryenExec>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.targets.js.binaryen.BinaryenExec>())
}
