package gradle.plugins.project

import gradle.api.ci.CI
import gradle.api.maybeNamed
import gradle.api.project.ProjectScript
import gradle.plugins.android.AndroidPlugin
import gradle.plugins.apivalidation.ApiValidationPlugin
import gradle.plugins.apple.ApplePlugin
import gradle.plugins.compose.ComposePlugin
import gradle.plugins.dokka.DokkaPlugin
import gradle.plugins.initialization.SLF4JProblemReporterContext
import gradle.plugins.karakum.KarakumPlugin
import gradle.plugins.knit.KnitPlugin
import gradle.plugins.kotlin.mpp.MPPPlugin
import gradle.plugins.kotlin.targets.jvm.JvmPlugin
import gradle.plugins.kover.KoverPlugin
import gradle.plugins.publish.PublishPlugin
import gradle.plugins.signing.SigningPlugin
import gradle.plugins.web.JsPlugin
import gradle.plugins.web.WasmJsPlugin
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink

public class ProjectPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(SLF4JProblemReporterContext()) {
        with(target) {
            // Load and apply project.yaml to build.gradle.kts.
            ProjectScript()

            pluginManager.apply(KoverPlugin::class.java)
            pluginManager.apply(DokkaPlugin::class.java)
            pluginManager.apply(KnitPlugin::class.java)
            pluginManager.apply(ApiValidationPlugin::class.java)
            pluginManager.apply(MPPPlugin::class.java)
            pluginManager.apply(AndroidPlugin::class.java)
            pluginManager.apply(ApplePlugin::class.java)
            pluginManager.apply(JvmPlugin::class.java)
            pluginManager.apply(JsPlugin::class.java)
            pluginManager.apply(KarakumPlugin::class.java)
            pluginManager.apply(WasmJsPlugin::class.java)
            pluginManager.apply(ComposePlugin::class.java)
            pluginManager.apply(PublishPlugin::class.java)
            pluginManager.apply(SigningPlugin::class.java)

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
