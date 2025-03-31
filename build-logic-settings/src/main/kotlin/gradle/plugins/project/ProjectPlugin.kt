@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.project

import com.android.build.gradle.internal.tasks.AndroidTestTask
import gradle.accessors.catalog.resolvePluginId
import gradle.accessors.kotlin
import gradle.accessors.projectProperties
import gradle.api.ci.CI
import gradle.api.maybeNamed
import gradle.api.repositories.CacheRedirector
import gradle.api.trySet
import gradle.plugins.android.AndroidPlugin
import gradle.plugins.animalsniffer.AnimalSnifferPlugin
import gradle.plugins.apivalidation.ApiValidationPlugin
import gradle.plugins.apple.ApplePlugin
import gradle.plugins.buildconfig.BuildConfigPlugin
import gradle.plugins.compose.ComposePlugin
import gradle.plugins.dependencycheck.DependencyCheckPlugin
import gradle.plugins.develocity.DevelocityPlugin
import gradle.plugins.doctor.DoctorPlugin
import gradle.plugins.dokka.DokkaPlugin
import gradle.plugins.initialization.SLF4JProblemReporterContext
import gradle.plugins.knit.KnitPlugin
import gradle.plugins.kotlin.allopen.AllOpenPlugin
import gradle.plugins.kotlin.apollo.ApolloPlugin
import gradle.plugins.kotlin.atomicfu.AtomicFUPlugin
import gradle.plugins.kotlin.benchmark.BenchmarkPlugin
import gradle.plugins.kotlin.cocoapods.CocoapodsPlugin
import gradle.plugins.kotlin.filterKotlinTargets
import gradle.plugins.kotlin.ksp.KspPlugin
import gradle.plugins.kotlin.ktorfit.KtorfitPlugin
import gradle.plugins.kotlin.mpp.MPPPlugin
import gradle.plugins.kotlin.noarg.NoArgPlugin
import gradle.plugins.kotlin.powerassert.PowerAssertPlugin
import gradle.plugins.kotlin.room.RoomPlugin
import gradle.plugins.kotlin.rpc.RpcPlugin
import gradle.plugins.kotlin.serialization.SerializationPlugin
import gradle.plugins.kotlin.sqldelight.SqlDelightPlugin
import gradle.plugins.kotlin.targets.jvm.JvmPlugin
import gradle.plugins.kotlin.targets.nat.android.KotlinAndroidNative32Target
import gradle.plugins.kotlin.targets.nat.android.KotlinAndroidNative64Target
import gradle.plugins.kotlin.targets.nat.android.KotlinAndroidNativeArm32Target
import gradle.plugins.kotlin.targets.nat.android.KotlinAndroidNativeArm64Target
import gradle.plugins.kotlin.targets.nat.android.KotlinAndroidNativeTarget
import gradle.plugins.kotlin.targets.nat.android.KotlinAndroidNativeX64Target
import gradle.plugins.kotlin.targets.nat.android.KotlinAndroidNativeX86Target
import gradle.plugins.kotlin.targets.nat.apple.KotlinAppleTarget
import gradle.plugins.kotlin.targets.nat.apple.ios.IosArm64Target
import gradle.plugins.kotlin.targets.nat.apple.ios.KotlinIosSimulatorArm64Target
import gradle.plugins.kotlin.targets.nat.apple.ios.KotlinIosTarget
import gradle.plugins.kotlin.targets.nat.apple.ios.KotlinIosX64Target
import gradle.plugins.kotlin.targets.nat.apple.macos.KotlinMacosArm64Target
import gradle.plugins.kotlin.targets.nat.apple.macos.KotlinMacosTarget
import gradle.plugins.kotlin.targets.nat.apple.macos.KotlinMacosX64Target
import gradle.plugins.kotlin.targets.nat.apple.tvos.KotlinTvosArm64Target
import gradle.plugins.kotlin.targets.nat.apple.tvos.KotlinTvosSimulatorArm64Target
import gradle.plugins.kotlin.targets.nat.apple.tvos.KotlinTvosTarget
import gradle.plugins.kotlin.targets.nat.apple.tvos.KotlinTvosX64Target
import gradle.plugins.kotlin.targets.nat.apple.watchos.KotlinWatchos32Target
import gradle.plugins.kotlin.targets.nat.apple.watchos.KotlinWatchos64Target
import gradle.plugins.kotlin.targets.nat.apple.watchos.KotlinWatchosArm32Target
import gradle.plugins.kotlin.targets.nat.apple.watchos.KotlinWatchosArm64Target
import gradle.plugins.kotlin.targets.nat.apple.watchos.KotlinWatchosDeviceArm64Target
import gradle.plugins.kotlin.targets.nat.apple.watchos.KotlinWatchosSimulatorArm64Target
import gradle.plugins.kotlin.targets.nat.apple.watchos.KotlinWatchosTarget
import gradle.plugins.kotlin.targets.nat.apple.watchos.KotlinWatchosX64Target
import gradle.plugins.kotlin.targets.nat.linux.KotlinLinuxArm64Target
import gradle.plugins.kotlin.targets.nat.linux.KotlinLinuxTarget
import gradle.plugins.kotlin.targets.nat.linux.KotlinLinuxX64Target
import gradle.plugins.kotlin.targets.nat.mingw.KotlinMingwTarget
import gradle.plugins.kotlin.targets.nat.mingw.KotlinMingwX64Target
import gradle.plugins.kover.KoverPlugin
import gradle.plugins.project.ProjectProperties.Companion.load
import gradle.plugins.project.ProjectProperties.Companion.yaml
import gradle.plugins.publish.PublishPlugin
import gradle.plugins.shadow.ShadowPlugin
import gradle.plugins.signing.SigningPlugin
import gradle.plugins.sonar.SonarPlugin
import gradle.plugins.spotless.SpotlessPlugin
import gradle.serialization.encodeToAny
import kotlinx.serialization.json.Json
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.internal.extensions.core.extra
import org.gradle.internal.extensions.stdlib.capitalized
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

public class ProjectPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(SLF4JProblemReporterContext()) {
        with(target) {
            // Load and apply project.yaml to build.gradle.kts properties.
            projectProperties = load().also { properties ->
                println("Load and apply $PROJECT_PROPERTIES_FILE to: $name")
                println(yaml.dump(Json.Default.encodeToAny(properties)))
            }

            ::setGroup trySet projectProperties.group
            ::setDescription trySet projectProperties.description
            version = projectProperties.version.toVersion().toString()

            // Apply here to be able to add plugin dependency to classpath.
            projectProperties.buildscript?.applyTo()

            // Apply plugins.
            projectProperties.plugins.ids?.map { id -> id.resolvePluginId() }?.forEach(plugins::apply)

            pluginManager.apply(DoctorPlugin::class.java)
            pluginManager.apply(DependencyCheckPlugin::class.java)
            pluginManager.apply(AnimalSnifferPlugin::class.java)
            pluginManager.apply(BuildConfigPlugin::class.java)
            pluginManager.apply(SpotlessPlugin::class.java)
            pluginManager.apply(KoverPlugin::class.java)
            pluginManager.apply(SonarPlugin::class.java)
            pluginManager.apply(DokkaPlugin::class.java)
            pluginManager.apply(KnitPlugin::class.java)
            pluginManager.apply(ShadowPlugin::class.java)
            pluginManager.apply(ApiValidationPlugin::class.java)
            pluginManager.apply(KspPlugin::class.java)
            pluginManager.apply(AllOpenPlugin::class.java)
            pluginManager.apply(NoArgPlugin::class.java)
            pluginManager.apply(AtomicFUPlugin::class.java)
            pluginManager.apply(SerializationPlugin::class.java)
            pluginManager.apply(BenchmarkPlugin::class.java)
            pluginManager.apply(SqlDelightPlugin::class.java)
            pluginManager.apply(RoomPlugin::class.java)
            pluginManager.apply(RpcPlugin::class.java)
            pluginManager.apply(KtorfitPlugin::class.java)
            pluginManager.apply(ApolloPlugin::class.java)
            pluginManager.apply(PowerAssertPlugin::class.java)
            pluginManager.apply(ApplePlugin::class.java) // doesn't depend on mpp
            pluginManager.apply(CocoapodsPlugin::class.java)
            pluginManager.apply(AndroidPlugin::class.java) // doesn't depend on mpp
            pluginManager.apply(MPPPlugin::class.java) // need android library or application plugin applied.
            pluginManager.apply(JvmPlugin::class.java) //  apply after mpp plugin to deal with jvm targets.
            pluginManager.apply(ComposePlugin::class.java)
            pluginManager.apply(PublishPlugin::class.java)
            pluginManager.apply(SigningPlugin::class.java)

            AndroidPlugin.adjustXmlFactories()

            projectProperties.nodeJsEnv?.applyTo()
            projectProperties.npm?.applyTo()
            projectProperties.yarn?.applyTo()
            projectProperties.yarnRootEnv?.applyTo()

            // Apply after node and yarn to adjust their downloadBaseUrls too.
            CacheRedirector.applyTo()

            // Apply root dependencies.
            projectProperties.dependencies?.let { dependencies ->
                dependencies {
                    dependencies.forEach { dependency ->
                        dependency.applyTo(this)
                    }
                }
            }

            configureLinkTasks()

            if (CI.present) {
                configureCI()
            }

            // Apply here to be able to configure all tasks including manually created.
            projectProperties.tasks?.forEach { task ->
                task.applyTo()
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

    private fun Project.configureCI() {
        projectProperties.cis?.forEach { ci ->
            val ciName = ci::class.simpleName!!.lowercase()

            tasks.register(ciName) {
                dependsOn(tasks.named("dependencyCheckAnalyze"))
                onlyIf { ci.dependenciesCheck }
            }

            tasks.register(ciName) {
                dependsOn(tasks.named("animalsnifferRelease"))
                onlyIf { ci.signaturesCheck }
            }

            tasks.register(ciName) {
                dependsOn(tasks.named("spotlessCheck"))
                onlyIf { ci.formatCheck }
            }

            tasks.register(ciName) {
                dependsOn(tasks.named("sonar"))
                onlyIf { ci.qualityCheck }
            }

            tasks.register(ciName) {
                dependsOn(tasks.named("check"))
                onlyIf { ci.test }
            }

            tasks.register(ciName) {
                dependsOn(tasks.named("koverVerify"))
                onlyIf { ci.coverageVerify }
            }

            tasks.register(ciName) {
                dependsOn(tasks.named("knitCheck"))
                onlyIf { ci.docSamplesCheck }
            }

            ci.publishRepositories.forEach { (name, enabled) ->
                val publishTaskName = "publishAllPublicationsTo${name.capitalized()}Repository"
                tasks.register("${ciName}${publishTaskName.capitalized()}") {
                    dependsOn(tasks.named(publishTaskName))
                    onlyIf { enabled }
                }
            }
        }

        configureTestTasksOnCI()
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
        projectProperties.kotlin?.targets
            .orEmpty()
            .filterKotlinTargets<T>()
            .mapNotNull(`gradle.plugins.kotlin`.KotlinTarget<*>::targetName)
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
