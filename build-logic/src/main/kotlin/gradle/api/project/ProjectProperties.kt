package gradle.api.project

import gradle.api.Properties
import gradle.api.project.ProjectProperties.Companion.PROJECT_PROPERTIES_EXT
import klib.data.type.primitives.string.scripting.ScriptConfig
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import klib.data.type.serialization.serializers.any.SerializableAny

@Serializable
public class ProjectProperties(
    override val config: ScriptConfig = ScriptConfig(),
    override val script: List<SerializableAny>
) : Properties() {

    public companion object {

        internal const val PROJECT_PROPERTIES_EXT = "project.properties.ext"

        private const val PROJECT_PROPERTIES_FILE = "project.yaml"

        context(project: Project)
        internal operator fun invoke() = with(project) {
            // Export extras.
            // enable Default Kotlin Hierarchy.
            extraProperties["kotlin.mpp.applyDefaultHierarchyTemplate"] = "true"
            // ios Compose uses UiKit, so we need to explicitly enable it, since it is experimental.
            extraProperties["org.jetbrains.compose.experimental.uikit.enabled"] = "true"

            extraProperties["generateBuildableXcodeproj.skipKotlinFrameworkDependencies"] = "true"

            // Load project.yaml.
            file(PROJECT_PROPERTIES_FILE)<ProjectProperties, Project>(project) { properties ->
                projectProperties = properties
            }
        }
    }
}

public var Project.projectProperties: ProjectProperties
    get() = extraProperties[PROJECT_PROPERTIES_EXT] as ProjectProperties
    private set(value) {
        this.properties
        extraProperties[PROJECT_PROPERTIES_EXT] = value
    }
