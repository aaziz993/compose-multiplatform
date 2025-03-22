package gradle.plugins.web.tasks

import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class AbstractSetupTask<T : org.jetbrains.kotlin.gradle.targets.js.AbstractSetupTask<*, *>> : DefaultTask<T>() {

    abstract val destinationHashFileProvider: String?

    context(Project)
    override fun applyTo(recipient: T) {
        super.applyTo(recipient)

        recipient.destinationHashFileProvider tryAssign destinationHashFileProvider?.let(::file)
    }
}

@Serializable
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
    override val name: String? = null,,
) : AbstractSetupTask<org.jetbrains.kotlin.gradle.targets.js.AbstractSetupTask<*, *>>() {

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<org.jetbrains.kotlin.gradle.targets.js.AbstractSetupTask<*, *>>())
}
