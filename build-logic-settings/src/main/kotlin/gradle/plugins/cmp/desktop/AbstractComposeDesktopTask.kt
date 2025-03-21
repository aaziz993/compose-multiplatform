package gradle.plugins.cmp.desktop

import gradle.api.tasks.DefaultTask
import gradle.api.tasks.Task
import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class AbstractComposeDesktopTask<T : org.jetbrains.compose.desktop.tasks.AbstractComposeDesktopTask>
    : DefaultTask<T>() {

    abstract val verbose: Boolean?

    context(Project)
    override fun applyTo(recipient: T) {
        super.applyTo(recipient)

        recipient.verbose tryAssign verbose
    }
}

@Serializable
@SerialName("AbstractComposeDesktopTask")
internal data class AbstractComposeDesktopTaskImpl(
    override val verbose: Boolean? = null,
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
    override val name: String = ""
) : AbstractComposeDesktopTask<org.jetbrains.compose.desktop.tasks.AbstractComposeDesktopTask>() {

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<org.jetbrains.compose.desktop.tasks.AbstractComposeDesktopTask>())
}
