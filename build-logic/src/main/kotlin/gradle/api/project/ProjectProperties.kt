package gradle.api.project

import gradle.api.Properties
import klib.data.type.primitives.string.scripting.ScriptConfig
import klib.data.type.serialization.serializers.any.SerializableAny
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties

public const val PROJECT_PROPERTIES_EXT: String = "project.properties.ext"

public const val PROJECT_PROPERTIES_FILE: String = "project.yaml"

@Serializable
public class ProjectProperties(
    public val layout: ProjectLayout = ProjectLayout.Flat(),
    override val config: ScriptConfig = ScriptConfig(),
    override val script: List<SerializableAny>,
    override val fileTree: Map<String, List<String>>,
) : Properties() {

    public companion object {
        context(project: Project)
        public operator fun invoke(): ProjectProperties = with(project) {
            // Export extras.
            // enable Default Kotlin Hierarchy.
            extraProperties["kotlin.mpp.applyDefaultHierarchyTemplate"] = "true"
            // ios Compose uses UiKit, so we need to explicitly enable it, since it is experimental.
            extraProperties["org.jetbrains.compose.experimental.uikit.enabled"] = "true"

            extraProperties["generateBuildableXcodeproj.skipKotlinFrameworkDependencies"] = "true"

            // Load project.yaml.
            file(PROJECT_PROPERTIES_FILE)<ProjectProperties, Project>(project)
                .also { properties -> projectProperties = properties }
                .also(ProjectProperties::invoke)
        }
    }
}

public var Project.projectProperties: ProjectProperties
    get() = extraProperties[PROJECT_PROPERTIES_EXT] as ProjectProperties
    private set(value) {
        extraProperties[PROJECT_PROPERTIES_EXT] = value
    }
