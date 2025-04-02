package gradle.api.initialization

import gradle.api.BaseProperties
import gradle.api.BaseProperties.Companion.exportExtraProperties
import gradle.api.BaseProperties.Companion.loadLocalProperties
import gradle.api.BaseProperties.Companion.load
import gradle.api.catalog.PluginNotationContentPolymorphicSerializer
import gradle.api.initialization.file.CodeOfConductFile
import gradle.api.initialization.file.ContributingFile
import gradle.api.initialization.file.LicenseFile
import gradle.api.initialization.file.LicenseHeaderFile
import gradle.api.initialization.file.ProjectFile
import gradle.api.publish.maven.MavenPomDeveloper
import gradle.api.publish.maven.MavenPomLicense
import gradle.api.publish.maven.MavenPomScm
import gradle.caching.BuildCacheConfiguration
import gradle.plugins.develocity.model.DevelocitySettings
import gradle.plugins.githooks.GitHooksExtension
import gradle.plugins.initialization.IncludeBuild
import gradle.plugins.toolchainmanagement.ToolchainManagement
import java.util.*
import klib.data.type.collection.DelegatedMapTransformingSerializer
import klib.data.type.collection.SerializableOptionalAnyMap
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.extra
import org.jetbrains.kotlin.gradle.plugin.extraProperties

@KeepGeneratedSerializer
@Serializable(with = InitializationPropertiesTransformingSerializer::class)
internal data class InitializationProperties(
    override val delegate: SerializableOptionalAnyMap,
    override val buildscript: ScriptHandler? = null,
    override val plugins: Set<@Serializable(with = PluginNotationContentPolymorphicSerializer::class) Any>? = null,
    override val cacheRedirector: Boolean = true,
    val remote: MavenPomScm? = null,
    val developer: MavenPomDeveloper? = null,
    val license: MavenPomLicense? = null,
    val licenseFile: LicenseFile? = null,
    val licenseHeaderFile: LicenseHeaderFile? = null,
    val codeOfConductFile: CodeOfConductFile? = null,
    val contributingFile: ContributingFile? = null,
    val projectFiles: Set<ProjectFile>? = null,
    val pluginManagement: PluginManagement? = null,
    val dependencyResolutionManagement: DependencyResolutionManagement? = null,
    val includes: Set<String>? = null,
    val includeFlats: Set<String>? = null,
    val includeBuilds: Set<IncludeBuild>? = null,
    val projects: Set<ProjectDescriptor>? = null,
    val buildCache: BuildCacheConfiguration? = null,
    val develocity: DevelocitySettings? = null,
    val toolchainManagement: ToolchainManagement? = null,
    val gitHooks: GitHooksExtension? = null,
) : BaseProperties {

    val includesPaths by lazy {
        includes?.map { include -> include.replace(":", System.lineSeparator()) }
    }

    companion object {

        private const val INITIALIZATION_PROPERTIES_FILE = "initialization.yaml"

        context(Settings)
        @Suppress("UnstableApiUsage")
        fun load() {
            // Load local.properties.
            settings.localProperties = loadLocalProperties(
                layout.settingsDirectory
                    .file("local.properties").asFile,
            )
            // Export extras.
            settings.extra.exportExtraProperties()
            // Load initialization.yaml.
            settings.initializationProperties = load(
                INITIALIZATION_PROPERTIES_FILE,
                settings.layout.settingsDirectory,
                settings,
            )
        }
    }
}

private object InitializationPropertiesTransformingSerializer :
    DelegatedMapTransformingSerializer<InitializationProperties>(
        InitializationProperties.generatedSerializer(),
    )

internal var Settings.localProperties: Properties
    get() = extraProperties[BaseProperties.LOCAL_PROPERTIES_EXT] as Properties
    private set(value) {
        extraProperties[BaseProperties.LOCAL_PROPERTIES_EXT] = value
    }

private const val INITIALIZATION_PROPERTIES_EXT = "initialization.properties.ext"

internal var Settings.initializationProperties: InitializationProperties
    get() = extraProperties[INITIALIZATION_PROPERTIES_EXT] as InitializationProperties
    private set(value) {
        extraProperties[INITIALIZATION_PROPERTIES_EXT] = value
    }
