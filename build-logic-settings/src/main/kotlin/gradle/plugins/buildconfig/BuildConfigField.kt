package gradle.plugins.buildconfig

import com.github.gmazzo.gradle.plugins.BuildConfigField
import gradle.api.ProjectNamed
import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class BuildConfigField(
    override val name: String? = null,
    val type: String? = null,
    val value: String? = null,
    val optional: Boolean? = null,
    val position: Int? = null,
) : ProjectNamed<BuildConfigField> {

    fun toBuildConfigField() =com.github.gmazzo.gradle.plugins. BuildConfigField(name, type, value, optional, position)

    context(Project)
    override fun applyTo(recipient: BuildConfigField) {
        recipient.type tryAssign type
        recipient.value tryAssign value
        recipient.optional tryAssign optional
        recipient.position tryAssign position
    }
}
