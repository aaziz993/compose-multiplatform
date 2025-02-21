@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project

import gradle.libs
import gradle.moduleProperties
import gradle.settings
import gradle.version
import gradle.versions
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.amper.gradle.SLF4JProblemReporterContext
import plugin.project.android.AndroidBindingPluginPart
import plugin.project.apple.AppleBindingPluginPart
import plugin.project.cocoapods.CocoapodsPluginPart
import plugin.project.compose.ComposePluginPart
import plugin.project.gradle.apivalidation.ApiValidationPluginPart
import plugin.project.gradle.buildconfig.BuildConfigPluginPart
import plugin.project.gradle.doctor.DoctorPluginPart
import plugin.project.gradle.dokka.DokkaPluginPart
import plugin.project.gradle.kover.KoverPluginPart
import plugin.project.gradle.sonar.SonarPluginPart
import plugin.project.gradle.spotless.SpotlessPluginPart
import plugin.project.jvm.JavaBindingPluginPart
import plugin.project.kotlin.allopen.AllOpenPluginPart
import plugin.project.kotlin.apollo.ApolloPluginPart
import plugin.project.kotlin.atomicfu.AtomicFUPluginPart
import plugin.project.kotlin.ksp.KspPluginPart
import plugin.project.kotlin.ktorfit.KtorfitPluginPart
import plugin.project.kotlin.noarg.NoArgPluginPart
import plugin.project.kotlin.powerassert.PowerAssertPluginPart
import plugin.project.kotlin.room.RoomPluginPart
import plugin.project.kotlin.rpc.RpcPluginPart
import plugin.project.kotlin.serialization.SerializationPluginPart
import plugin.project.kotlin.sqldelight.SqlDelightPluginPart
import plugin.project.web.WasmBindingPluginPart
import plugin.project.web.js.JsBindingPluginPart
import plugin.project.web.node.configureNodeJsRootExtension
import plugin.project.web.npm.configureNpmExtension
import plugin.project.web.yarn.configureYarnRootExtension

/**
 * Gradle project plugin entry point.
 */
internal class BindingProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(SLF4JProblemReporterContext()) {
        with(target) {
            moduleProperties.group?.let(::setGroup)
            moduleProperties.description?.let(::setDescription)
            settings.libs.versions.let { versions ->
                version(
                    versions.version("${target.name}-version-major")?.toInt() ?: 1,
                    versions.version("${target.name}-version-minor")?.toInt() ?: 0,
                    versions.version("${target.name}-version-patch")?.toInt() ?: 0,
                    versions.version("${target.name}-version-preRelease"),
                )
            }

            plugins.apply(KMPBindingPluginPart::class.java)

            afterEvaluate {
                plugins.apply(DoctorPluginPart::class.java)
                plugins.apply(BuildConfigPluginPart::class.java)
                plugins.apply(SpotlessPluginPart::class.java)
                plugins.apply(KoverPluginPart::class.java)
                plugins.apply(SonarPluginPart::class.java)
                plugins.apply(DokkaPluginPart::class.java)
                plugins.apply(ApiValidationPluginPart::class.java)
                plugins.apply(KspPluginPart::class.java)
                plugins.apply(AllOpenPluginPart::class.java)
                plugins.apply(NoArgPluginPart::class.java)
                plugins.apply(AtomicFUPluginPart::class.java)
                plugins.apply(SerializationPluginPart::class.java)
                plugins.apply(SqlDelightPluginPart::class.java)
                plugins.apply(RoomPluginPart::class.java)
                plugins.apply(RpcPluginPart::class.java)
                plugins.apply(KtorfitPluginPart::class.java)
                plugins.apply(ApolloPluginPart::class.java)
                plugins.apply(PowerAssertPluginPart::class.java)
                plugins.apply(ComposePluginPart::class.java)
                plugins.apply(AndroidBindingPluginPart::class.java)
                plugins.apply(JsBindingPluginPart::class.java)
                plugins.apply(WasmBindingPluginPart::class.java)
                plugins.apply(JavaBindingPluginPart::class.java)
                plugins.apply(AppleBindingPluginPart::class.java)
                plugins.apply(CocoapodsPluginPart::class.java)
            }
        }

        // Apply other settings.

//        applyRepositoryAttributes(project)
//        applyPublicationAttributes(linkedModule, project)
//        applyTest(linkedModule, project)

        if (problemReporter.getErrors().isNotEmpty()) {
            throw GradleException(problemReporter.getGradleError())
        }
    }

    private fun Project.configureWeb() {
        configureNodeJsRootExtension()
        configureNpmExtension()
        configureYarnRootExtension()
    }

    private fun applyRepositoryAttributes(
        project: Project
    ) = with(project) {
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
