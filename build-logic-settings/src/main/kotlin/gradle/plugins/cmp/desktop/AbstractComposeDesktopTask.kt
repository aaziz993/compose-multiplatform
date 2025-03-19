package gradle.plugins.cmp.desktop


import gradle.api.tasks.Task
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class AbstractComposeDesktopTask : Task {

    abstract val verbose: Boolean?

        context(Project)
    override fun applyTo(named: T) {
        super.applyTo(named)

        named as org.jetbrains.compose.desktop.tasks.AbstractComposeDesktopTask

        named.verbose tryAssign verbose
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<org.jetbrains.compose.desktop.tasks.AbstractComposeDesktopTask>())
}

@Serializable
@SerialName("AbstractComposeDesktopTask")
internal data class AbstractComposeDesktopTaskImpl(
    override val verbose: Boolean? = null,
    override val dependsOn: SortedSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: SortedSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String = ""
) : AbstractComposeDesktopTask()
