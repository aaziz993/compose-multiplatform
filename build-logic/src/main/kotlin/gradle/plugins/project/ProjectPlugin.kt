package gradle.plugins.project

import com.android.build.api.dsl.androidLibrary
import gradle.api.ci.CI
import gradle.api.maybeNamed
import gradle.api.project.ProjectProperties
import gradle.api.project.android
import gradle.api.project.androidNamespace
import gradle.api.project.kotlin
import gradle.api.project.libs
import gradle.api.project.moduleName
import gradle.plugins.initialization.SLF4JProblemReporterContext
import klib.data.type.primitives.toInt
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
import org.jetbrains.kotlin.gradle.utils.loadPropertyFromResources

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