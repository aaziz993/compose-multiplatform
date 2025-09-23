package gradle.api.project

import gradle.api.GradleScript
import java.io.File
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
    public val group: String,
    public val version: SemanticVersion = SemanticVersion(),
    public val description: String = "",
    override val config: ScriptConfig = ScriptConfig(),
    override val script: List<SerializableAny>,
    override val fileTree: Map<String, List<String>>,
) : GradleScript() {

    public companion object {

        context(project: Project)
        public operator fun invoke(): Unit = with(project) {
            file(PROJECT_PROPERTIES_FILE).takeIf(File::exists)?.invoke<ProjectScript, Project>(project)
                .let { properties ->
                    projectScript = properties ?: ProjectScript(group = "", script = emptyList(), fileTree = emptyMap())
                    if (properties != null) {
                        group = properties.group
                        version = properties.version.toVersion().toString()
                        description = properties.description
                        properties()
                    }
                }
        }
    }
}

public var Project.projectScript: ProjectScript
    get() = extraProperties[PROJECT_PROPERTIES_EXT] as ProjectScript
    private set(value) {
        extraProperties[PROJECT_PROPERTIES_EXT] = value
    }
