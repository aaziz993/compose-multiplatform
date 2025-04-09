package gradle.plugins.kotlin.targets.web.tasks

import org.gradle.kotlin.dsl.withType
import gradle.accessors.files
import gradle.api.provider.tryAssign
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import klib.data.type.reflection.tryPlus
import klib.data.type.reflection.trySet
import klib.data.type.serialization.serializer.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal abstract class UnpackSkikoWasmRuntimeTask<T : org.jetbrains.compose.web.tasks.UnpackSkikoWasmRuntimeTask>
    : DefaultTask<T>() {

    abstract val skikoRuntimeFiles: Set<String>?
    abstract val setSkikoRuntimeFiles: Set<String>?

    abstract val outputDir: String?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver::skikoRuntimeFiles tryPlus skikoRuntimeFiles?.let(project::files)
        receiver::skikoRuntimeFiles trySet setSkikoRuntimeFiles?.let(project::files)
        receiver.outputDir tryAssign outputDir?.let(project.layout.projectDirectory::dir)
    }
}

@Serializable
@SerialName("UnpackSkikoWasmRuntimeTask")
internal data class UnpackSkikoWasmRuntimeTaskImpl(
    override val skikoRuntimeFiles: Set<String>? = null,
    override val setSkikoRuntimeFiles: Set<String>? = null,
    override val outputDir: String? = null,
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
) : UnpackSkikoWasmRuntimeTask<org.jetbrains.compose.web.tasks.UnpackSkikoWasmRuntimeTask>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.compose.web.tasks.UnpackSkikoWasmRuntimeTask>())
}
