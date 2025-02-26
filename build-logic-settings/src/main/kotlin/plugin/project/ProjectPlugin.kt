@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project

import gradle.libs
import gradle.moduleName
import gradle.projectProperties
import gradle.settings
import gradle.version
import gradle.versions
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.amper.gradle.BindingProjectPlugin
import org.jetbrains.amper.gradle.SLF4JProblemReporterContext
import plugin.model.dependency.Dependency
import plugin.project.android.AndroidPlugin
import plugin.project.apple.ApplePlugin
import plugin.project.compose.ComposePlugin
import plugin.project.gradle.apivalidation.ApiValidationPlugin
import plugin.project.gradle.buildconfig.BuildConfigPlugin
import plugin.project.gradle.doctor.DoctorPlugin
import plugin.project.gradle.dokka.DokkaPlugin
import plugin.project.gradle.kover.KoverPlugin
import plugin.project.gradle.sonar.SonarPlugin
import plugin.project.gradle.spotless.SpotlessPlugin
import plugin.project.java.JavaPlugin
import plugin.project.kotlin.allopen.AllOpenPlugin
import plugin.project.kotlin.apollo.ApolloPlugin
import plugin.project.kotlin.atomicfu.AtomicFUPlugin
import plugin.project.kotlin.kmp.KMPPlugin
import plugin.project.kotlin.ksp.KspPlugin
import plugin.project.kotlin.ktorfit.KtorfitPlugin
import plugin.project.kotlin.noarg.NoArgPlugin
import plugin.project.kotlin.powerassert.PowerAssertPlugin
import plugin.project.kotlin.room.RoomPlugin
import plugin.project.kotlin.rpc.RpcPlugin
import plugin.project.kotlin.serialization.SerializationPlugin
import plugin.project.kotlin.sqldelight.SqlDelightPlugin
import plugin.project.nat.NativePlugin
import plugin.project.web.WasmPlugin
import plugin.project.web.WasmWasiPlugin
import plugin.project.web.js.JsPlugin
import plugin.project.web.node.configureNodeJsEnvSpec
import plugin.project.web.npm.configureNpmExtension
import plugin.project.web.yarn.configureYarnRootExtension

/**
 * Gradle project plugin entry point.
 */
internal class ProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(SLF4JProblemReporterContext()) {
        with(target) {
            if (projectProperties.kotlin.targets?.isNotEmpty() == true) {
                projectProperties.group?.let(::setGroup)
                projectProperties.description?.let(::setDescription)
                version = settings.libs.versions.let { versions ->
                    val moduleName = moduleName
                    version(
                        versions.version("$moduleName.version.major")?.toInt() ?: 1,
                        versions.version("$moduleName.version.minor")?.toInt() ?: 0,
                        versions.version("$moduleName.version.patch")?.toInt() ?: 0,
                        versions.version("$moduleName.version.preRelease"),
                    )
                }
            }

            //  Don't change order!
            plugins.apply(DoctorPlugin::class.java)
            plugins.apply(BuildConfigPlugin::class.java)
            plugins.apply(SpotlessPlugin::class.java)
            plugins.apply(KoverPlugin::class.java)
            plugins.apply(SonarPlugin::class.java)
            plugins.apply(DokkaPlugin::class.java)
            plugins.apply(ApiValidationPlugin::class.java)
            plugins.apply(AllOpenPlugin::class.java)
            plugins.apply(NoArgPlugin::class.java)
            plugins.apply(AtomicFUPlugin::class.java)
            plugins.apply(SerializationPlugin::class.java)
            plugins.apply(SqlDelightPlugin::class.java)
            plugins.apply(RoomPlugin::class.java)
            plugins.apply(RpcPlugin::class.java)
            plugins.apply(KtorfitPlugin::class.java)
            plugins.apply(ApolloPlugin::class.java)
            plugins.apply(PowerAssertPlugin::class.java)
            plugins.apply(AndroidPlugin::class.java) // apply and configure android library or application plugin.
            plugins.apply(KMPPlugin::class.java) // need android library or application plugin applied.
            plugins.apply(KspPlugin::class.java) // kspCommonMainMetadata need kmp plugin applied.
            plugins.apply(JavaPlugin::class.java)
            plugins.apply(NativePlugin::class.java)
            plugins.apply(ApplePlugin::class.java)
            plugins.apply(JsPlugin::class.java)
            plugins.apply(WasmPlugin::class.java)
            plugins.apply(WasmWasiPlugin::class.java)
            plugins.apply(ComposePlugin::class.java)

//            projectProperties.dependencies?.let { dependencies ->
//                dependencies.filterIsInstance<Dependency>().forEach { dependency ->
//                    dependencies {
//                        dependency.applyTo(this)
//                    }
//                }
//            }
        }

        // Apply other settings.

//        applyPublicationAttributes(linkedModule, project)
//        applyTest(linkedModule, project)

        if (problemReporter.getErrors().isNotEmpty()) {
            throw GradleException(problemReporter.getGradleError())
        }
    }

    private fun Project.configureWeb() {
        configureNodeJsEnvSpec()
        configureNpmExtension()
        configureYarnRootExtension()
    }

    private fun Project.applyRepositoryAttributes(
    ) {
        repositories.mavenCentral()
    }

//    private fun applyPublicationAttributes(
//        module: AmperModuleWrapper,
//        project: Project
//    ) {
//        project.plugins.apply("maven-publish")
//        val extension = project.extensions.getByType(PublishingExtension::class.java)
//        module.leafNonTestFragments.firstOrNull()?.settings?.publishing?.let { settings ->
//            // TODO Handle artifacts with different coordinates, or move "PublicationArtifactPart" to module part.
//            project.group = settings.group ?: ""
//            project.version = settings.version ?: ""
//            extension.repositories.configure(module.parts.find<RepositoriesModulePart>(), all = false)
//            if (settings.name != null) {
//                /**
//                 * Currently override only -jvm part of the publication
//                 * In non-gradle implementation, we'll be most likely publishing only one GAV per jvm-only library
//                 * (i.e. without KMP)
//                 */
//                val publication = extension.publications
//                    .filterIsInstance<MavenPublication>()
//                    .singleOrNull { it.artifactId.endsWith("-jvm") }
//                check(publication != null) {
//                    "-jvm artifact publishing must exist for module ${module.userReadableName}"
//                }
//
//                publication.artifactId = settings.name
//            }
//        }
//    }
//
//    /**
//     * [all] - if we need to configure all repositories which can be used for resolve, or only publishing ones.
//     */
//    private fun RepositoryHandler.configure(part: RepositoriesModulePart?, all: Boolean = true) {
//        val repositories = part?.mavenRepositories?.filter { (all && it.resolve) || it.publish } ?: return
//        repositories.forEach { declared ->
//            if (declared.id == "mavenLocal" && declared.url == "mavenLocal") {
//                mavenLocal()
//            }
//            else {
//                maven {
//                    name = declared.id
//                    url = URI.create(declared.url)
//                    if (declared.userName != null && declared.password != null) {
//                        credentials {
//                            username = declared.userName
//                            password = declared.password
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    @Suppress("UNCHECKED_CAST")
//    private fun applyTest(linkedModule: AmperModuleWrapper, project: Project) {
//        if (linkedModule.leafTestFragments.any { it.settings.junit == JUnitVersion.JUNIT5 }) {
//            project.tasks.withType(Test::class.java) {
//                // TODO Add more comprehensive support - only enable for those tasks,
//                //   that relate to fragment.
//                useJUnitPlatform()
//            }
//        }
//
//        project.configureTest()
//    }
}
