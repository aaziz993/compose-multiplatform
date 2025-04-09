package gradle.plugins.kotlin.benchmark.tasks

import org.gradle.kotlin.dsl.withType
import gradle.accessors.files
import gradle.api.file.tryFrom
import gradle.api.file.trySetFrom
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import klib.data.type.reflection.tryPlus
import klib.data.type.reflection.trySet
import klib.data.type.serialization.serializer.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal abstract class WasmSourceGeneratorTask<T : kotlinx.benchmark.gradle.WasmSourceGeneratorTask>
    : DefaultTask<T>() {

    abstract val inputClassesDirs: Set<String>?

    abstract val setInputClassesDirs: Set<String>?

    abstract val inputDependencies: Set<String>?

    abstract val setInputDependencies: Set<String>?

    abstract val outputResourcesDir: String?

    abstract val outputSourcesDir: String?

    abstract val runtimeClasspath: Set<String>?

    abstract val setRuntimeClasspath: Set<String>?

    abstract val title: kotlin.String?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver::inputClassesDirs tryPlus inputClassesDirs?.let(project::files)
        receiver::inputClassesDirs trySet setInputClassesDirs?.let(project::files)
        receiver::inputDependencies tryPlus inputDependencies?.let(project::files)
        receiver::inputDependencies trySet setInputDependencies?.let(project::files)
        receiver::outputResourcesDir trySet outputResourcesDir?.let(project::file)
        receiver::outputSourcesDir trySet outputSourcesDir?.let(project::file)
        receiver.runtimeClasspath tryFrom runtimeClasspath?.let(project::files)
        receiver.runtimeClasspath trySetFrom setRuntimeClasspath?.let(project::files)
        receiver::title trySet title
    }
}

@Serializable
@SerialName("WasmSourceGeneratorTask")
internal data class WasmSourceGeneratorTaskImpl(
    override val inputClassesDirs: Set<String>? = null,
    override val setInputClassesDirs: Set<String>? = null,
    override val inputDependencies: Set<String>? = null,
    override val setInputDependencies: Set<String>? = null,
    override val outputResourcesDir: String? = null,
    override val outputSourcesDir: String? = null,
    override val runtimeClasspath: Set<String>? = null,
    override val setRuntimeClasspath: Set<String>? = null,
    override val title: String? = null,
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
) : WasmSourceGeneratorTask<kotlinx.benchmark.gradle.WasmSourceGeneratorTask>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<kotlinx.benchmark.gradle.WasmSourceGeneratorTask>())
}
