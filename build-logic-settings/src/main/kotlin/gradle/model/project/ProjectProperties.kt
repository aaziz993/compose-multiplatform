@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.model.project

import gradle.allLibs
import gradle.deepMerge
import gradle.isUrl
import gradle.libs
import gradle.model.Task
import gradle.model.TaskTransformingSerializer
import gradle.model.kotlin.kmp.jvm.android.BaseExtension
import gradle.model.kotlin.kmp.jvm.android.application.BaseAppModuleExtension
import gradle.model.kotlin.kmp.jvm.android.library.LibraryExtension
import gradle.model.kotlin.kmp.jvm.java.JavaPluginExtension
import gradle.model.kotlin.kmp.jvm.java.application.JavaApplication
import gradle.model.kotlin.HasKotlinDependencies
import gradle.model.kotlin.KotlinSettings
import gradle.model.kotlin.kmp.web.js.karakum.KarakumSettings
import gradle.model.kotlin.kmp.web.node.model.NodeJsEnvSpec
import gradle.model.kotlin.kmp.web.npm.model.NpmExtension
import gradle.model.kotlin.kmp.web.yarn.model.YarnRootExtension
import gradle.problemreporter.SLF4JProblemReporterContext
import gradle.projectProperties
import gradle.resolve
import gradle.serialization.decodeFromAny
import gradle.serialization.encodeToAny
import gradle.settings
import gradle.toVersionCatalogUrlPath
import gradle.trySetSystemProperty
import gradle.version
import gradle.versions
import java.net.URI
import java.util.Properties
import javax.xml.stream.XMLEventFactory
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory
import kotlin.io.reader
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCollection
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.artifacts.repositories.DefaultMavenArtifactRepository
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import org.jetbrains.compose.internal.utils.currentTarget
import org.jetbrains.compose.jetbrainsCompose
import org.tomlj.Toml
import org.yaml.snakeyaml.Yaml
import plugin.PROJECT_PROPERTIES_FILE
import plugin.project.android.AndroidPlugin
import plugin.project.apple.ApplePlugin
import plugin.project.apple.model.AppleSettings
import plugin.project.cmp.CMPPlugin
import plugin.project.cmp.model.CMPSettings
import plugin.project.gradle.apivalidation.ApiValidationPlugin
import plugin.project.gradle.buildconfig.BuildConfigPlugin
import plugin.project.gradle.develocity.DevelocityPlugin
import plugin.project.gradle.doctor.DoctorPlugin
import plugin.project.gradle.dokka.DokkaPlugin
import plugin.project.gradle.githooks.GitHooksPlugin
import plugin.project.gradle.kover.KoverPlugin
import plugin.project.gradle.shadow.ShadowPlugin
import plugin.project.gradle.sonar.SonarPlugin
import plugin.project.gradle.spotless.SpotlessPlugin
import plugin.project.gradle.toolchainmanagement.ToolchainManagementPlugin
import plugin.project.java.JavaPlugin
import plugin.project.kotlin.kmp.KMPPlugin
import plugin.project.kotlin.allopen.AllOpenPlugin
import plugin.project.kotlin.apollo.ApolloPlugin
import plugin.project.kotlin.atomicfu.AtomicFUPlugin
import plugin.project.kotlin.ksp.KspPlugin
import plugin.project.kotlin.ktorfit.KtorfitPlugin
import plugin.project.kotlin.noarg.NoArgPlugin
import plugin.project.kotlin.powerassert.PowerAssertPlugin
import plugin.project.kotlin.room.RoomPlugin
import plugin.project.kotlin.rpc.RpcPlugin
import plugin.project.kotlin.serialization.SerializationPlugin
import plugin.project.kotlin.sqldelight.SqlDelightPlugin
import plugin.project.nat.NativePlugin
import plugin.project.web.JsPlugin
import plugin.project.web.WasmPlugin
import plugin.project.web.WasmWasiPlugin

private const val VERSION_CATALOG_CACHE_DIR = "build-logic-settings/gradle"

private const val COMPOSE_VERSION_CATALOG_FILE = "build-logic-settings/gradle/compose.versions.template.toml"

@Serializable
internal data class ProjectProperties(
    val pluginManagement: PluginManagement? = null,
    val dependencyResolutionManagement: DependencyResolutionManagement? = null,
    override val dependencies: List<@Serializable(with = DependencyTransformingSerializer::class) Dependency>? = null,
    val includes: List<String>? = null,
    val projects: List<ProjectDescriptor>? = null,
    val gradleEnterpriseAccessKey: String? = null,
    val type: ProjectType = ProjectType.LIB,
    val layout: ProjectLayout = ProjectLayout.DEFAULT,
    val group: String? = null,
    val description: String? = null,
    val version: VersionSettings = VersionSettings(),
    val plugins: Plugins = Plugins(),
    val jvm: JavaPluginExtension? = null,
    val application: JavaApplication? = null,
    val kotlin: KotlinSettings = KotlinSettings(),
    @Transient
    var android: BaseExtension? = null,
    val apple: AppleSettings = AppleSettings(),
    val nodeJsEnv: NodeJsEnvSpec = NodeJsEnvSpec(),
    val yarn: YarnRootExtension = YarnRootExtension(),
    val npm: NpmExtension = NpmExtension(),
    val karakum: KarakumSettings = KarakumSettings(),
    val compose: CMPSettings = CMPSettings(),
    val tasks: List<@Serializable(with = TaskTransformingSerializer::class) Task>? = null,
    private val localPropertiesFile: String = "../local.properties",
) : HasKotlinDependencies {

    @Transient
    val localProperties = Properties()

    context(Settings)
    @Suppress("UnstableApiUsage")
    fun applyTo() {
        pluginManagement.let { pluginManagement ->
            pluginManagement {
                repositories {
                    mavenCentral()
                    google()
                    gradlePluginPortal()

                    pluginManagement?.repositories?.forEach { repository -> maven(repository) }
                }
            }
        }

        dependencyResolutionManagement?.let { dependencyResolutionManagement ->
            dependencyResolutionManagement {
                repositories {
                    settings.applyTo(this)
                }

                val libsFile = settings.layout.settingsDirectory.file("gradle/libs.versions.toml").asFile
                if (dependencyResolutionManagement.versionCatalogs?.none { it.name == "libs" } != false && libsFile.exists())
                    allLibs += "libs" to Toml.parse(libsFile.readText())


                dependencyResolutionManagement.versionCatalogs?.let { versionCatalogs ->
                    versionCatalogs {
                        versionCatalogs.forEach { (catalogName, dependency) ->
                            var notation = dependency.resolve()

                            create(catalogName) {
                                from(notation)
                            }


                            if (notation is FileCollection) {
                                allLibs[catalogName] = Toml.parse(notation.files.single().readText())
                            }
                            else {
                                notation as String

                                val cacheFile = settings.layout.settingsDirectory.file("$VERSION_CATALOG_CACHE_DIR/$catalogName.versions.toml").asFile

                                if (cacheFile.exists()) {
                                    allLibs[catalogName] = Toml.parse(cacheFile.readText())
                                    return@forEach
                                }

                                if (!notation.isUrl) {
                                    notation = notation.toVersionCatalogUrlPath()
                                }

                                allLibs[catalogName] = repositories.map { (it as DefaultMavenArtifactRepository).url.toString().removeSuffix("/") }
                                    .firstNotNullOf { url ->
                                        try {
                                            Toml.parse(
                                                URI(
                                                    "$url/$notation",
                                                ).toURL()
                                                    .readText()
                                                    .also(cacheFile::writeText), // write to cache
                                            )
                                        }
                                        catch (_: Throwable) {
                                            null
                                        }
                                    }
                            }
                        }
                    }
                }

                // Load pre-defined compose version catalog
                allLibs += "compose" to Toml.parse(
                    settings.layout.settingsDirectory.file(COMPOSE_VERSION_CATALOG_FILE).asFile
                        .readText()
                        .replace("\$kotlin", libs.versions.version("kotlin")!!)
                        .replace("\$compose", libs.versions.version("compose")!!)
                        .replace("\$materialExtendedIcon", libs.versions.version("materialExtendedIcon")!!)
                        .replace("\$currentTargetId", currentTarget.id),
                )
            }
        }

        buildscript.repositories {
            settings.applyTo(this)
        }

        // Apply plugins after version catalogs are loaded in settings phase.
        settings.plugins.apply(DevelocityPlugin::class.java)
        settings.plugins.apply(ToolchainManagementPlugin::class.java)
        settings.plugins.apply(GitHooksPlugin::class.java)

        // Include subprojects.
        includes?.forEach(::include)

        // Configure subprojects.
        projects?.forEach { project ->
            project.applyTo()
        }
    }

    context(Project)
    fun applyTo() = with(SLF4JProblemReporterContext()) {
        if (kotlin.targets?.isNotEmpty() == true) {
            group?.let(::setGroup)
            description?.let(::setDescription)

            project.version = version()
        }

        buildscript.repositories {
            settings.applyTo(this)
        }

        //  Don't change order!
        project.plugins.apply(DoctorPlugin::class.java)
        project.plugins.apply(BuildConfigPlugin::class.java)
        project.plugins.apply(SpotlessPlugin::class.java)
        project.plugins.apply(KoverPlugin::class.java)
        project.plugins.apply(SonarPlugin::class.java)
        project.plugins.apply(DokkaPlugin::class.java)
        project.plugins.apply(ShadowPlugin::class.java)
        project.plugins.apply(ApiValidationPlugin::class.java)
        project.plugins.apply(AllOpenPlugin::class.java)
        project.plugins.apply(NoArgPlugin::class.java)
        project.plugins.apply(AtomicFUPlugin::class.java)
        project.plugins.apply(SerializationPlugin::class.java)
        project.plugins.apply(SqlDelightPlugin::class.java)
        project.plugins.apply(RoomPlugin::class.java)
        project.plugins.apply(RpcPlugin::class.java)
        project.plugins.apply(KtorfitPlugin::class.java)
        project.plugins.apply(ApolloPlugin::class.java)
        project.plugins.apply(PowerAssertPlugin::class.java)
        project.plugins.apply(JavaPlugin::class.java)
        project.plugins.apply(AndroidPlugin::class.java) // apply and configure android library or application plugin.
        project.plugins.apply(KMPPlugin::class.java) // need android library or application plugin applied.
        project.plugins.apply(KspPlugin::class.java) // kspCommonMainMetadata need kmp plugin applied.
        project.plugins.apply(NativePlugin::class.java)
        project.plugins.apply(ApplePlugin::class.java)
        project.plugins.apply(JsPlugin::class.java)
        project.plugins.apply(WasmPlugin::class.java)
        project.plugins.apply(WasmWasiPlugin::class.java)
        project.plugins.apply(CMPPlugin::class.java)

        dependencies?.forEach { dependency ->
            dependencies {
                dependency.applyTo(this)
            }
        }

        tasks?.forEach { task ->
            task.applyTo()
        }

        if (problemReporter.getErrors().isNotEmpty()) {
            throw GradleException(problemReporter.getGradleError())
        }

        afterEvaluate {
            // W/A for XML factories mess within apple plugin classpath.
            val hasAndroidPlugin = plugins.hasPlugin("com.android.application") ||
                plugins.hasPlugin("com.android.library")
            if (hasAndroidPlugin) {
                adjustXmlFactories()
            }
        }
    }

    /**
     * W/A for service loading conflict between apple plugin
     * and android plugin.
     */
    private fun adjustXmlFactories() {
        trySetSystemProperty(
            XMLInputFactory::class.qualifiedName!!,
            "com.sun.xml.internal.stream.XMLInputFactoryImpl",
        )
        trySetSystemProperty(
            XMLOutputFactory::class.qualifiedName!!,
            "com.sun.xml.internal.stream.XMLOutputFactoryImpl",
        )
        trySetSystemProperty(
            XMLEventFactory::class.qualifiedName!!,
            "com.sun.xml.internal.stream.events.XMLEventFactoryImpl",
        )
    }

    private fun Settings.applyTo(handler: RepositoryHandler) = with(handler) {
        mavenCentral()
        // For the Android plugin and dependencies
        google()
        // For other Gradle plugins
        gradlePluginPortal()
        // For dev versions of kotlin
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
        // For dev versions of compose plugin and dependencies
        jetbrainsCompose()
        // For compose experimental builds
        maven("https://packages.jetbrains.team/maven/p/firework/dev")
        // Sonatype OSS Snapshot Repository
        maven("https://oss.sonatype.org/content/repositories/snapshots")

        // Apply repositories from project properties.
        projectProperties.dependencyResolutionManagement?.repositories?.let { repositories ->
            repositories.forEach { repository ->
                maven(repository)
            }
        }
    }

    companion object {

        private val json = Json { ignoreUnknownKeys = true }

        private val yaml = Yaml()

        context(Settings)
        @Suppress("UnstableApiUsage")
        fun applyTo() {
            projectProperties = layout.settingsDirectory.load()
            projectProperties.applyTo()
        }

        context(Project)
        fun applyTo() {
            projectProperties = layout.projectDirectory.load().apply {
                println("Applied $PROJECT_PROPERTIES_FILE to: $name")
                println(yaml.dump(Json.Default.encodeToAny(this)))
            }

            projectProperties.applyTo()
        }

        @Suppress("UNCHECKED_CAST")
        private fun Directory.load(): ProjectProperties {
            val propertiesFile = file(PROJECT_PROPERTIES_FILE).asFile

            return if (propertiesFile.exists()) {

                val properties = yaml.load<MutableMap<String, *>>(propertiesFile.readText())

                val templatedProperties = (properties["templates"] as List<String>?)?.fold(emptyMap<String, Any?>()) { acc, template ->
                    acc deepMerge yaml.load<MutableMap<String, *>>(file(template).asFile.readText())
                }.orEmpty() deepMerge properties

                json.decodeFromAny<ProjectProperties>(templatedProperties.resolve()).apply {
                    if (type == ProjectType.APP)
                        android = templatedProperties["android"]?.let { json.decodeFromAny<BaseAppModuleExtension>(it) }
                    else
                        android = templatedProperties["android"]?.let { json.decodeFromAny<LibraryExtension>(it) }
                }
            }
            else {
                ProjectProperties()
            }.apply {
                localProperties.apply {
                    val file = file(localPropertiesFile).asFile
                    if (file.exists()) {
                        load(file.reader())
                    }
                }
            }
        }
    }
}
