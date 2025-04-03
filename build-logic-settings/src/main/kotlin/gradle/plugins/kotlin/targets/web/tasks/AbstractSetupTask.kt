package gradle.plugins.kotlin.targets.web.tasks

import gradle.api.file.tryAssign
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import klib.data.type.serialization.serializer.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class AbstractSetupTask<T : org.jetbrains.kotlin.gradle.targets.js.AbstractSetupTask<*, *>> : DefaultTask<T>() {

    abstract val destinationHashFileProvider: String?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver.destinationHashFileProvider tryAssign destinationHashFileProvider?.let(project::file)
    }
}

@Serializable
@SerialName("AbstractSetupTask")
internal data class AbstractSetupTaskImpl(
    override val destinationHashFileProvider: String? = null,
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
) : AbstractSetupTask<org.jetbrains.kotlin.gradle.targets.js.AbstractSetupTask<*, *>>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.targets.js.AbstractSetupTask<*, *>>())
}
