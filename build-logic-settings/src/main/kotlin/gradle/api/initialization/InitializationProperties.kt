package gradle.api.initialization

import gradle.api.Properties
import gradle.api.Properties.Companion.exportExtraProperties
import gradle.api.Properties.Companion.loadLocalProperties
import gradle.api.Properties.Companion.load
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
import gradle.plugins.initialization.Apply
import gradle.plugins.initialization.IncludeBuild
import gradle.plugins.toolchainmanagement.ToolchainManagement
import klib.data.type.serialization.serializer.MapSerializer
import klib.data.type.serialization.serializer.NullableAnySerializer
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.extra
import org.jetbrains.kotlin.gradle.plugin.extraProperties

@Serializable(with = InitializationPropertiesMapSerializer::class)
internal interface InitializationProperties : Properties {

    val year: String?

    val remote: MavenPomScm?

    val developer: MavenPomDeveloper?

    val license: MavenPomLicense?

    val licenseFile: LicenseFile?

    val licenseHeaderFile: LicenseHeaderFile?

    val codeOfConductFile: CodeOfConductFile?

    val contributingFile: ContributingFile?

    val projectFiles: Set<ProjectFile>?

    val pluginManagement: PluginManagement?

    val applies: Set<Apply>?

    val dependencyResolutionManagement: DependencyResolutionManagement?

    val includes: Set<String>?

    val includeFlats: Set<String>?

    val includeBuilds: Set<IncludeBuild>?

    val projects: Set<ProjectDescriptor>?

    val buildCache: BuildCacheConfiguration?

    val develocity: DevelocitySettings?

    val toolchainManagement: ToolchainManagement?

    val gitHooks: GitHooksExtension?

    val includesPaths
        get() = includes?.map { include -> include.replace(":", System.lineSeparator()) }

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

private object InitializationPropertiesMapSerializer :
    MapSerializer<Any?, InitializationProperties, InitializationPropertiesImpl>(
        InitializationPropertiesImpl.serializer(),
        NullableAnySerializer,
    )

@Serializable
private data class InitializationPropertiesImpl(
    override val buildscript: ScriptHandler? = null,
    override val plugins: Set<Any>? = null,
    override val cacheRedirector: Boolean = true,
    override val year: String? = null,
    override val remote: MavenPomScm? = null,
    override val developer: MavenPomDeveloper? = null,
    override val license: MavenPomLicense? = null,
    override val licenseFile: LicenseFile? = null,
    override val licenseHeaderFile: LicenseHeaderFile? = null,
    override val codeOfConductFile: CodeOfConductFile? = null,
    override val contributingFile: ContributingFile? = null,
    override val projectFiles: Set<ProjectFile>? = null,
    override val pluginManagement: PluginManagement? = null,
    override val applies: Set<Apply>? = null,
    override val dependencyResolutionManagement: DependencyResolutionManagement? = null,
    override val includes: Set<String>? = null,
    override val includeFlats: Set<String>? = null,
    override val includeBuilds: Set<IncludeBuild>? = null,
    override val projects: Set<ProjectDescriptor>? = null,
    override val buildCache: BuildCacheConfiguration? = null,
    override val develocity: DevelocitySettings? = null,
    override val toolchainManagement: ToolchainManagement? = null,
    override val gitHooks: GitHooksExtension? = null,
) : InitializationProperties, MutableMap<String, Any?> by mutableMapOf()

internal var Settings.localProperties: java.util.Properties
    get() = extraProperties[Properties.LOCAL_PROPERTIES_EXT] as java.util.Properties
    private set(value) {
        extraProperties[Properties.LOCAL_PROPERTIES_EXT] = value
    }

private const val INITIALIZATION_PROPERTIES_EXT = "initialization.properties.ext"

internal var Settings.initializationProperties: InitializationProperties
    get() = extraProperties[INITIALIZATION_PROPERTIES_EXT] as InitializationProperties
    private set(value) {
        extraProperties[INITIALIZATION_PROPERTIES_EXT] = value
    }
