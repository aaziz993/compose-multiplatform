@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.model

import gradle.allLibs
import gradle.isUrl
import gradle.libs
import gradle.projectProperties
import gradle.settings
import gradle.trySetSystemProperty
import gradle.version
import gradle.versions
import java.net.URI
import javax.xml.stream.XMLEventFactory
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory
import kotlinx.serialization.Serializable
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.file.FileCollection
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.artifacts.repositories.DefaultMavenArtifactRepository
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import org.jetbrains.compose.internal.utils.currentTarget
import org.jetbrains.compose.jetbrainsCompose
import org.tomlj.Toml
import plugin.model.dependency.Dependency
import plugin.model.dependency.ProjectDependency
import plugin.model.dependency.ProjectDependencyTransformingSerializer
import plugin.model.dependency.toVersionCatalogUrlPath
import plugin.project.android.AndroidPlugin
import plugin.project.android.model.AndroidSettings
import plugin.project.apple.ApplePlugin
import plugin.project.apple.model.AppleSettings
import plugin.project.compose.ComposePlugin
import plugin.project.compose.model.ComposeSettings
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
import plugin.project.java.model.JavaPluginExtension
import plugin.project.java.model.application.JavaApplication
import plugin.project.kotlin.allopen.AllOpenPlugin
import plugin.project.kotlin.apollo.ApolloPlugin
import plugin.project.kotlin.atomicfu.AtomicFUPlugin
import plugin.project.kotlin.kmp.KMPPlugin
import plugin.project.kotlin.kmp.model.KotlinMultiplatformSettings
import plugin.project.kotlin.ksp.KspPlugin
import plugin.project.kotlin.ktorfit.KtorfitPlugin
import plugin.project.kotlin.model.HasKotlinDependencies
import plugin.project.kotlin.model.Task
import plugin.project.kotlin.model.TaskTransformingSerializer
import plugin.project.kotlin.noarg.NoArgPlugin
import plugin.project.kotlin.powerassert.PowerAssertPlugin
import plugin.project.kotlin.room.RoomPlugin
import plugin.project.kotlin.rpc.RpcPlugin
import plugin.project.kotlin.serialization.SerializationPlugin
import plugin.project.kotlin.sqldelight.SqlDelightPlugin
import plugin.project.nat.NativePlugin
import plugin.project.problemreporter.SLF4JProblemReporterContext
import plugin.project.web.WasmPlugin
import plugin.project.web.WasmWasiPlugin
import plugin.project.web.js.JsPlugin
import plugin.project.web.js.karakum.model.KarakumSettings
import plugin.project.web.node.model.NodeJsEnvSpec
import plugin.project.web.npm.model.NpmExtension
import plugin.project.web.yarn.model.YarnRootExtension

private const val VERSION_CATALOG_CACHE_DIR = "build-logic-settings/gradle"

private const val COMPOSE_VERSION_CATALOG_FILE = "build-logic-settings/gradle/compose.versions.template.toml"

@Serializable
internal data class ProjectProperties(
    val pluginManagement: PluginManagement? = null,
    val dependencyResolutionManagement: DependencyResolutionManagement? = null,
    override val dependencies: List<@Serializable(with = ProjectDependencyTransformingSerializer::class) ProjectDependency>? = null,
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
    val kotlin: KotlinMultiplatformSettings = KotlinMultiplatformSettings(),
    val android: AndroidSettings = AndroidSettings(),
    val apple: AppleSettings = AppleSettings(),
    val nodeJsEnv: NodeJsEnvSpec = NodeJsEnvSpec(),
    val yarn: YarnRootExtension = YarnRootExtension(),
    val npm: NpmExtension = NpmExtension(),
    val karakum: KarakumSettings = KarakumSettings(),
    val compose: ComposeSettings = ComposeSettings(),
    val tasks: List<@Serializable(with = TaskTransformingSerializer::class) Task>? = null,
) : HasKotlinDependencies {

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
                            var notation = (dependency as Dependency).resolve()

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
        project.plugins.apply(AndroidPlugin::class.java) // apply and configure android library or application plugin.
        project.plugins.apply(KMPPlugin::class.java) // need android library or application plugin applied.
        project.plugins.apply(KspPlugin::class.java) // kspCommonMainMetadata need kmp plugin applied.
        project.plugins.apply(JavaPlugin::class.java)
        project.plugins.apply(NativePlugin::class.java)
        project.plugins.apply(ApplePlugin::class.java)
        project.plugins.apply(JsPlugin::class.java)
        project.plugins.apply(WasmPlugin::class.java)
        project.plugins.apply(WasmWasiPlugin::class.java)
        project.plugins.apply(ComposePlugin::class.java)

        dependencies?.filterIsInstance<Dependency>()?.forEach { dependency ->
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
}
