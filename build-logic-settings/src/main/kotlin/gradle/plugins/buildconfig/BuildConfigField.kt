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

    context(project: Project)
    override fun applyTo(receiver: BuildConfigField) {
        receiver.type tryAssign type
        receiver.value tryAssign value
        receiver.optional tryAssign optional
        receiver.position tryAssign position
    }
}
