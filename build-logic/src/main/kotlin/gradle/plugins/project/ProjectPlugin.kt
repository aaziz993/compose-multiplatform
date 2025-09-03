package gradle.plugins.project

import gradle.api.artifacts.dsl.signature
import gradle.api.artifacts.dsl.testImplementation
import gradle.api.ci.CI
import gradle.api.initialization.localProperties
import gradle.api.initialization.settingsProperties
import gradle.api.maybeNamed
import gradle.api.plugins.id
import gradle.api.project.ProjectProperties
import gradle.api.project.allOpen
import gradle.api.project.apiValidation
import gradle.api.project.apollo
import gradle.api.project.benchmark
import gradle.api.project.cocoapods
import gradle.api.project.dependencyCheck
import gradle.api.project.dokka
import gradle.api.project.kotlin
import gradle.api.project.kover
import gradle.api.project.ktorfit
import gradle.api.project.noArg
import gradle.api.project.powerAssert
import gradle.api.project.rpc
import gradle.api.project.sonar
import gradle.api.project.spotless
import gradle.api.project.sqldelight
import gradle.plugins.dokka.html
import gradle.plugins.initialization.SLF4JProblemReporterContext
import gradle.plugins.spotless.formats
import kotlinx.benchmark.gradle.JvmBenchmarkTarget
import kotlinx.rpc.RpcStrictMode
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
import gradle.api.project.*
import org.gradle.api.publish.maven.MavenPublication
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

public class ProjectPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(SLF4JProblemReporterContext()) {
        with(target) {
            // Load and apply project.yaml to build.gradle.kts.
            ProjectProperties()

            configureLinkTasks()
            
            CI.configureTasks()

            if (problemReporter.getErrors().isNotEmpty()) {
                throw GradleException(problemReporter.getGradleError())
            }
        }
    }

    @Suppress("UnstableApiUsage")
    private fun Project.configureLinkTasks() =
        pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
            tasks.register("linkAll") {
                dependsOn(tasks.withType<KotlinNativeLink>())
            }

            val os = OperatingSystem.current()

            // Run native tests only on matching host.
            // There is no need to configure `onlyIf` for Darwin targets as they're configured by KGP.
            tasks.maybeNamed("linkDebugTestLinuxX64") {
                onlyIf("run only on Linux") { os == OperatingSystem.LINUX }
            }
            tasks.maybeNamed("linkDebugTestLinuxArm64") {
                onlyIf("run only on Linux") { os == OperatingSystem.LINUX }
            }
            tasks.maybeNamed("linkDebugTestMingwX64") {
                onlyIf("run only on Windows") { os == OperatingSystem.WINDOWS }
            }
        }
}