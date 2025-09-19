package gradle.api.project

import gradle.api.GradleScript
import klib.data.type.primitives.string.scripting.ScriptConfig
import klib.data.type.serialization.serializers.any.SerializableAny
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties

public const val PROJECT_PROPERTIES_EXT: String = "project.properties.ext"

public const val PROJECT_PROPERTIES_FILE: String = "project.yaml"

@Serializable
public class ProjectScript(
    public val layout: ProjectLayout = ProjectLayout.Flat(),
    override val config: ScriptConfig = ScriptConfig(),
    override val script: List<SerializableAny>,
    override val fileTree: Map<String, List<String>>,
) : GradleScript() {

    public companion object {
        context(project: Project)
        public operator fun invoke(): ProjectScript = with(project) {
            // ios Compose uses UiKit, so we need to explicitly enable it, since it is experimental.
            extraProperties["org.jetbrains.compose.experimental.uikit.enabled"] = "true"

            // Load project.yaml.
            file(PROJECT_PROPERTIES_FILE)<ProjectScript, Project>(project)
                .also { properties -> projectScript = properties }
                .also(ProjectScript::invoke)
        }
    }
}

public var Project.projectScript: ProjectScript
    get() = extraProperties[PROJECT_PROPERTIES_EXT] as ProjectScript
    private set(value) {
        extraProperties[PROJECT_PROPERTIES_EXT] = value
    }
