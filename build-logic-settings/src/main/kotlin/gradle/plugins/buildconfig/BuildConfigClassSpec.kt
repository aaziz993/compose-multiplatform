package gradle.plugins.buildconfig

import gradle.api.ProjectNamed
import gradle.api.applyTo
import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal interface BuildConfigClassSpec<T : com.github.gmazzo.gradle.plugins.BuildConfigClassSpec> : ProjectNamed<T> {

    val className: String?

    val packageName: String?

    val buildConfigFields: List<BuildConfigField>?

    context(Project)
    override fun applyTo(recipient: T) {
        recipient.className tryAssign className
        recipient.packageName tryAssign packageName

        buildConfigFields?.forEach { buildConfigField ->
            buildConfigField.applyTo(recipient.buildConfigFields)
        }
    }
}

@Serializable
internal data class BuildConfigClassSpecImpl(
    override val className: String? = null,
    override val packageName: String? = null,
    override val buildConfigFields: List<BuildConfigField>? = null,
    override val name: String = ""
) : BuildConfigClassSpec<com.github.gmazzo.gradle.plugins.BuildConfigClassSpec>
