@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugins.project

import com.android.build.gradle.internal.tasks.AndroidTestTask
import gradle.accessors.kotlin
import gradle.accessors.projectProperties
import gradle.api.applyTo
import gradle.api.isCI
import gradle.api.maybeNamed
import gradle.api.repositories.CacheRedirector
import gradle.api.version
import gradle.plugins.kmp.filterKotlinTargets
import gradle.plugins.kmp.nat.android.KotlinAndroidNative32Target
import gradle.plugins.kmp.nat.android.KotlinAndroidNative64Target
import gradle.plugins.kmp.nat.android.KotlinAndroidNativeArm32Target
import gradle.plugins.kmp.nat.android.KotlinAndroidNativeArm64Target
import gradle.plugins.kmp.nat.android.KotlinAndroidNativeTarget
import gradle.plugins.kmp.nat.android.KotlinAndroidNativeX64Target
import gradle.plugins.kmp.nat.android.KotlinAndroidNativeX86Target
import gradle.plugins.kmp.nat.apple.KotlinAppleTarget
import gradle.plugins.kmp.nat.apple.ios.IosArm64Target
import gradle.plugins.kmp.nat.apple.ios.KotlinIosSimulatorArm64Target
import gradle.plugins.kmp.nat.apple.ios.KotlinIosTarget
import gradle.plugins.kmp.nat.apple.ios.KotlinIosX64Target
import gradle.plugins.kmp.nat.apple.macos.KotlinMacosArm64Target
import gradle.plugins.kmp.nat.apple.macos.KotlinMacosTarget
import gradle.plugins.kmp.nat.apple.macos.KotlinMacosX64Target
import gradle.plugins.kmp.nat.apple.tvos.KotlinTvosArm64Target
import gradle.plugins.kmp.nat.apple.tvos.KotlinTvosSimulatorArm64Target
import gradle.plugins.kmp.nat.apple.tvos.KotlinTvosTarget
import gradle.plugins.kmp.nat.apple.tvos.KotlinTvosX64Target
import gradle.plugins.kmp.nat.apple.watchos.KotlinWatchos32Target
import gradle.plugins.kmp.nat.apple.watchos.KotlinWatchos64Target
import gradle.plugins.kmp.nat.apple.watchos.KotlinWatchosArm32Target
import gradle.plugins.kmp.nat.apple.watchos.KotlinWatchosArm64Target
import gradle.plugins.kmp.nat.apple.watchos.KotlinWatchosDeviceArm64Target
import gradle.plugins.kmp.nat.apple.watchos.KotlinWatchosSimulatorArm64Target
import gradle.plugins.kmp.nat.apple.watchos.KotlinWatchosTarget
import gradle.plugins.kmp.nat.apple.watchos.KotlinWatchosX64Target
import gradle.plugins.kmp.nat.linux.KotlinLinuxArm64Target
import gradle.plugins.kmp.nat.linux.KotlinLinuxTarget
import gradle.plugins.kmp.nat.linux.KotlinLinuxX64Target
import gradle.plugins.kmp.nat.mingw.KotlinMingwTarget
import gradle.plugins.kmp.nat.mingw.KotlinMingwX64Target
import gradle.project.PROJECT_PROPERTIES_FILE
import gradle.project.ProjectProperties.Companion.load
import gradle.project.ProjectProperties.Companion.yaml
import gradle.serialization.encodeToAny
import kotlinx.serialization.json.Json
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.internal.extensions.core.extra
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmWasiTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
import org.jetbrains.kotlin.gradle.tasks.KotlinTest
import plugins.android.AndroidPlugin
import plugins.animalsniffer.AnimalSnifferPlugin
import plugins.apivalidation.ApiValidationPlugin
import plugins.apple.ApplePlugin
import plugins.buildconfig.BuildConfigPlugin
import plugins.cmp.CMPPlugin
import plugins.dependencycheck.DependencyCheckPlugin
import plugins.develocity.DevelocityPlugin
import plugins.doctor.DoctorPlugin
import plugins.dokka.DokkaPlugin
import plugins.initialization.problemreporter.SLF4JProblemReporterContext
import plugins.java.JavaPlugin
import plugins.kmp.KMPPlugin
import plugins.knit.KnitPlugin
import plugins.kotlin.allopen.AllOpenPlugin
import plugins.kotlin.apollo.ApolloPlugin
import plugins.kotlin.atomicfu.AtomicFUPlugin
import plugins.kotlin.benchmark.BenchmarkPlugin
import plugins.kotlin.ksp.KspPlugin
import plugins.kotlin.ktorfit.KtorfitPlugin
import plugins.kotlin.noarg.NoArgPlugin
import plugins.kotlin.powerassert.PowerAssertPlugin
import plugins.kotlin.room.RoomPlugin
import plugins.kotlin.rpc.RpcPlugin
import plugins.kotlin.serialization.SerializationPlugin
import plugins.kotlin.sqldelight.SqlDelightPlugin
import plugins.kover.KoverPlugin
import plugins.nat.NativePlugin
import plugins.publish.PublishPlugin
import plugins.shadow.ShadowPlugin
import plugins.signing.SigningPlugin
import plugins.sonar.SonarPlugin
import plugins.spotless.SpotlessPlugin
import plugins.web.JsPlugin
import plugins.web.WasmPlugin
import plugins.web.WasmWasiPlugin

public class ProjectPlugin : Plugin<Project> {

    @Suppress("UnstableApiUsage")
    override fun apply(target: Project): Unit = with(SLF4JProblemReporterContext()) {
        with(target) {
            // Load and apply project.yaml to build.gradle.kts properties.
            projectProperties = load().also { properties ->
                println("Load and apply $PROJECT_PROPERTIES_FILE to: $name")
                println(yaml.dump(Json.Default.encodeToAny(properties)))
            }

            projectProperties.buildscript?.applyTo()

            if (projectProperties.kotlin.targets.isNotEmpty()) {
                projectProperties.group?.let(::setGroup)
                projectProperties.description?.let(::setDescription)
                project.version = version()
            }

            //  Don't change order!
            project.plugins.apply(DoctorPlugin::class.java)
            project.plugins.apply(DependencyCheckPlugin::class.java)
            project.plugins.apply(BuildConfigPlugin::class.java)
            project.plugins.apply(SpotlessPlugin::class.java)
            project.plugins.apply(KoverPlugin::class.java)
            project.plugins.apply(SonarPlugin::class.java)
            project.plugins.apply(DokkaPlugin::class.java)
            project.plugins.apply(KnitPlugin::class.java) // apply after dokka plugin to make knitPrepare be dependOn dokkaGenerate.
            project.plugins.apply(ShadowPlugin::class.java)
            project.plugins.apply(ApiValidationPlugin::class.java)
            project.plugins.apply(AllOpenPlugin::class.java)
            project.plugins.apply(NoArgPlugin::class.java)
            project.plugins.apply(AtomicFUPlugin::class.java)
            project.plugins.apply(SerializationPlugin::class.java)
            project.plugins.apply(BenchmarkPlugin::class.java)
            project.plugins.apply(SqlDelightPlugin::class.java)
            project.plugins.apply(RoomPlugin::class.java)
            project.plugins.apply(RpcPlugin::class.java)
            project.plugins.apply(KtorfitPlugin::class.java)
            project.plugins.apply(ApolloPlugin::class.java)
            project.plugins.apply(PowerAssertPlugin::class.java)
            project.plugins.apply(ApplePlugin::class.java) // doesn't depend on kmp
            project.plugins.apply(AndroidPlugin::class.java) // apply and configure android library or application plugin.
            project.plugins.apply(AnimalSnifferPlugin::class.java)
            project.plugins.apply(KMPPlugin::class.java) // need android library or application plugin applied.
            project.plugins.apply(JavaPlugin::class.java) //  apply after kmp plugin.
            project.plugins.apply(KspPlugin::class.java) // kspCommonMainMetadata need kmp plugin applied.
            project.plugins.apply(NativePlugin::class.java)
            project.plugins.apply(JsPlugin::class.java)
            project.plugins.apply(WasmPlugin::class.java)
            project.plugins.apply(WasmWasiPlugin::class.java)
            project.plugins.apply(CMPPlugin::class.java)
            project.plugins.apply(PublishPlugin::class.java)
            project.plugins.apply(SigningPlugin::class.java)

            projectProperties.nodeJsEnv.applyTo()
            projectProperties.npm.applyTo()
            projectProperties.yarn.applyTo()
            projectProperties.yarnRootEnv.applyTo()

            CacheRedirector.applyTo()

            projectProperties.dependencies?.forEach { dependency ->
                dependencies {
                    dependency.applyTo(this)
                }
            }

            projectProperties.tasks?.forEach { task ->
                task.applyTo()
            }

            configureLinkTasks()

            if (isCI) {
                configureTestTasksOnCI()
            }

            if (problemReporter.getErrors().isNotEmpty()) {
                throw GradleException(problemReporter.getGradleError())
            }
        }
    }

    @Suppress("UnstableApiUsage")
    private fun Project.configureLinkTasks() {
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

    /** Applies CI-specific configurations to test tasks. */
    private fun Project.configureTestTasksOnCI() {
        // Don't fail build on the CI:
        // 1. To distinct builds failed because of failed tests and because of compilation errors or anything else.
        //    TeamCity parses test results to define build status, so the build won't be green.
        // 2. To run as many tests as possible while keeping fail-fast behavior locally.
        tasks.withType<AbstractTestTask>().configureEach {
            ignoreFailures = true
            if (this is KotlinTest) ignoreRunFailures = true
        }

        // KotlinTestReport overwrites ignoreFailure values and fails build on test failure if this flag is disabled
        extra["kotlin.tests.individualTaskReports"] = true

        tasks.withType<KotlinJvmTest>().configureEach {
            DevelocityPlugin.testRetry {
                maxRetries = 1
                maxFailures = 10
            }

            applyTestRetryCompatibilityWorkaround()
        }

        registerAggregationTestTasks()
    }

    /**
     * Test retry plugin is incompatible with test tasks that override the `createTestExecuter` method.
     * This includes the [KotlinJvmTest] task which wraps the test executor with its own wrapper.
     *
     * This workaround heavily relies on the internal implementation details of the test-retry plugin and KGP.
     *
     * The test retry plugin adds a `doFirst` action, which:
     * - Retrieves the test executer using `createTestExecuter` (KGP returns wrapped test executer here)
     * - Wraps it with `RetryTestExecuter`
     * - Sets the executer using `setTestExecuter`
     *
     * In the `doLast` action, it expects that `createTestExecuter` returns the previously created `RetryTestExecuter` instance.
     * However, KGP wraps every result of `createTestExecutor` with its own wrapper, resulting in the following nesting:
     *   KotlinJvmTarget$Executer(RetryTestExecuter(KotlinJvmTarget$Executer(DefaultTestExecuter)))
     *
     * KGP wraps the executer only if `targetName` is present, as it is needed to add the target name suffix to the test name.
     * The workaround sets `targetName` to `null` after the first KGP wrapper is created,
     * so `createTestExecuter` returns the previously created executer:
     *   RetryTestExecuter(KotlinJvmTarget$Executer(DefaultTestExecuter))
     *
     * Issue: https://github.com/gradle/test-retry-gradle-plugin/issues/116 (KT-49155)
     */
    private fun KotlinJvmTest.applyTestRetryCompatibilityWorkaround() {
        if (targetName == null) return
        val originalTargetName = targetName

        val executeTestsActionIndex = taskActions.indexOfLast { it.displayName == "Execute executeTests" }
        check(executeTestsActionIndex != -1) { "Action executeTests not found" }

        // Add the workaround action and then move it to the correct position right before tests execution.
        doFirst("workaround for compatibility with testRetry") { targetName = null }
        val injectedAction = taskActions.removeFirst()
        taskActions.add(executeTestsActionIndex, injectedAction)

        // Restore targetName value as other plugins might rely on it.
        // For example, kover uses it to find test tasks by target name
        doLast("restore targetName") { targetName = originalTargetName }
    }

    private fun Project.registerAggregationTestTasks() {
        registerAggregationTestTask<KotlinJvmTest>(
            "jvmAll",
            { it is KotlinJvmTarget },
        )

        // test only on min and max JDK versions
        registerAggregationTestTask<KotlinJvmTest>(
            "jvmJdkRange",
            { it is KotlinJvmTarget },
        ) {
            it.matching { it.javaLauncher.get().metadata.languageVersion.asInt() in setOf(8, 23) }
        }

        registerAggregationTestTask<KotlinJvmTest>(
            "androidAll",
            { it is KotlinAndroidTarget },
        )

        registerAggregationTestTask(
            "connectedAndroid",
            { it is KotlinAndroidTarget },
        ) { tasks.matching { it is AndroidTestTask && it.name.startsWith("connected", ignoreCase = true) } }

        // Android native
        registerAggregationNativeTestTask<KotlinAndroidNativeArm32Target>("androidNativeArm32All")
        registerAggregationNativeTestTask<KotlinAndroidNativeX86Target>("androidNativeX86All")
        registerAggregationNativeTestTask<KotlinAndroidNative32Target>("androidNative32")
        registerAggregationNativeTestTask<KotlinAndroidNativeArm64Target>("androidNativeArm64All")
        registerAggregationNativeTestTask<KotlinAndroidNativeX64Target>("androidNativeX64All")
        registerAggregationNativeTestTask<KotlinAndroidNative64Target>("androidNative64")
        registerAggregationNativeTestTask<KotlinAndroidNativeTarget>("androidNative")

        // Darwin
        // ios
        registerAggregationNativeTestTask<IosArm64Target>("iosArm64All")
        registerAggregationNativeTestTask<KotlinIosX64Target>("iosX64All")
        registerAggregationNativeTestTask<KotlinIosSimulatorArm64Target>("iosSimulatorArm64All")
        registerAggregationNativeTestTask<KotlinIosTarget>("ios")
        // watchos
        registerAggregationNativeTestTask<KotlinWatchosArm32Target>("watchosArm32All")
        registerAggregationNativeTestTask<KotlinWatchosArm64Target>("watchosArm64All")
        registerAggregationNativeTestTask<KotlinWatchos32Target>("watchos32")
        registerAggregationNativeTestTask<KotlinWatchosDeviceArm64Target>("watchosDeviceArm64All")
        registerAggregationNativeTestTask<KotlinWatchosX64Target>("watchosX64All")
        registerAggregationNativeTestTask<KotlinWatchosSimulatorArm64Target>("watchosSimulatorArm64All")
        registerAggregationNativeTestTask<KotlinWatchos64Target>("watchos64")
        registerAggregationNativeTestTask<KotlinWatchosTarget>("watchos")
        // tvos
        registerAggregationNativeTestTask<KotlinTvosArm64Target>("tvosArm64All")
        registerAggregationNativeTestTask<KotlinTvosX64Target>("tvosX64All")
        registerAggregationNativeTestTask<KotlinTvosSimulatorArm64Target>("tvosSimulatorArm64All")
        registerAggregationNativeTestTask<KotlinTvosTarget>("tvos")
        // macos
        registerAggregationNativeTestTask<KotlinMacosTarget>("macos")
        registerAggregationNativeTestTask<KotlinMacosArm64Target>("macosArm64All")
        registerAggregationNativeTestTask<KotlinMacosX64Target>("macosX64All")
        registerAggregationNativeTestTask<KotlinMacosTarget>("macos")
        // apple
        registerAggregationNativeTestTask<KotlinAppleTarget>("apple")

        // Linux
        registerAggregationNativeTestTask<KotlinLinuxArm64Target>("linuxArm64All")
        registerAggregationNativeTestTask<KotlinLinuxX64Target>("linuxX64All")
        registerAggregationNativeTestTask<KotlinLinuxTarget>("linux")

        // Windows
        registerAggregationNativeTestTask<KotlinMingwTarget>("mingw")
        registerAggregationNativeTestTask<KotlinMingwX64Target>("mingwX64All")

        // Native
        registerAggregationTestTask<KotlinNativeTest>(
            "native",
            { it is KotlinNativeTarget },
        ) {
            it.matching(KotlinNativeTest::isEnabled)
        }

        registerAggregationTestTask<KotlinJsTest>(
            "jsAll",
            { it::class == KotlinJsTargetDsl::class },
        )

        registerAggregationTestTask<KotlinJsTest>(
            "wasmAll",
            { it is KotlinWasmTargetDsl },
        )

        registerAggregationTestTask<KotlinJsTest>(
            "jsCommon",
            { it is KotlinJsTargetDsl },
        )

        registerAggregationTestTask<KotlinJsTest>(
            "wasmWasiAll",
            { it is KotlinWasmWasiTargetDsl },
        )
    }

    private inline fun <reified T : Task> Project.registerAggregationTestTask(
        name: String,
        noinline targetFilter: (KotlinTarget) -> Boolean,
        crossinline tasksFilter: (TaskCollection<T>) -> TaskCollection<T> = { it },
    ) = kotlin.targets
        .matching(targetFilter)
        .map(KotlinTarget::targetName)
        .let { targetNames ->
            registerAggregationTestTask(
                name,
                targetNames,
            ) {
                tasksFilter(tasks.withType<T>())
            }
        }

    private inline fun <reified T : Any> Project.registerAggregationNativeTestTask(name: String) =
        projectProperties.kotlin.targets
            .filterKotlinTargets<T>()
            .mapNotNull(`gradle.plugins.kmp`.KotlinTarget<*>::targetName)
            .let { targetNames ->
                registerAggregationTestTask<KotlinNativeTest>(
                    name,
                    targetNames,
                ) {
                    tasks.withType<KotlinNativeTest>().matching { test ->
                        test.enabled && test.targetName?.let { targetName -> targetName in targetNames } != false
                    }
                }
            }

    private inline fun <reified T : Task> Project.registerAggregationTestTask(
        name: String,
        targetNames: List<String>,
        tasksFilter: (TaskCollection<T>) -> TaskCollection<T> = { it },
    ) {
        if (targetNames.isEmpty()) return

        tasksFilter(
            tasks.withType<T>().matching { task ->
                targetNames.any { targetName -> task.name.startsWith(targetName) }
            },
        ).takeIf(TaskCollection<*>::isNotEmpty)
            ?.let { testTasks ->
                tasks.register("${name}Test") {
                    group = "verification"

                    dependsOn(testTasks)
                }
            }
    }
}
