@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project

import gradle.moduleProperties
import gradle.libs
import gradle.settings
import gradle.version
import gradle.versions
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.project.android.AndroidBindingPluginPart
import plugin.project.apple.AppleBindingPluginPart
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
import plugin.project.kotlin.sqldelight.SqlDelightPluginPart
import plugin.project.web.WasmBindingPluginPart
import plugin.project.web.js.JsBindingPluginPart

/**
 * Gradle project plugin entry point.
 */
internal class BindingProjectPlugin : Plugin<Project> {

    lateinit var appliedParts: List<BindingPluginPart>

    override fun apply(project: Project)  {
        // Find applied parts. Preserve order!
        val registeredParts = listOf<BindingPluginPart>(
            DoctorPluginPart(project),
            BuildConfigPluginPart(project),
            SpotlessPluginPart(project),
            KoverPluginPart(project),
            SonarPluginPart(project),
            DokkaPluginPart(project),
            ApiValidationPluginPart(project),
            KspPluginPart(project),
            AllOpenPluginPart(project),
            NoArgPluginPart(project),
            AtomicFUPluginPart(project),
//            SerializationPluginPart(project),
            SqlDelightPluginPart(project),
            RoomPluginPart(project),
            RpcPluginPart(project),
            KtorfitPluginPart(project),
            ApolloPluginPart(project),
            PowerAssertPluginPart(project),
            AndroidBindingPluginPart(project),
            JsBindingPluginPart(project),
            WasmBindingPluginPart(project),
            JavaBindingPluginPart(project),
            AppleBindingPluginPart(project),
            KMPPBindingPluginPart(project),
            ComposePluginPart(project),
        )
        appliedParts = registeredParts.filter { it.needToApply }

        // Apply after evaluate (For example to access Amper extension).
        // But register handlers first, so they will run before all other, that are registered
        // within "beforeEvaluate".
        project.afterEvaluate {
            appliedParts.forEach(BindingPluginPart::applyAfterEvaluate)
        }

        // Apply before evaluate.
        appliedParts.forEach(BindingPluginPart::applyBeforeEvaluate)

        // Apply other settings.
        with(project) {
            moduleProperties.group?.let(::setGroup)
            moduleProperties.description?.let(::setDescription)
            settings.libs.versions.let { versions ->
                version(
                    versions.version("${project.name}-version-major")?.toInt() ?: 1,
                    versions.version("${project.name}-version-minor")?.toInt() ?: 0,
                    versions.version("${project.name}-version-patch")?.toInt() ?: 0,
                    versions.version("${project.name}-version-preRelease"),
                )
            }
        }
        applyRepositoryAttributes(linkedModule, project)
//        applyPublicationAttributes(linkedModule, project)
        applyTest(linkedModule, project)

        if (problemReporter.getErrors().isNotEmpty()) {
            throw GradleException(problemReporter.getGradleError())
        }
    }

    private fun applyRepositoryAttributes(
        module: AmperModuleWrapper,
        project: Project
    ) = with(project){
        repositories.configure(module.parts.find<RepositoriesModulePart>())
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
