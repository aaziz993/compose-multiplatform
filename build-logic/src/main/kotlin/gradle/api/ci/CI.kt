package gradle.api.ci

import com.android.build.gradle.internal.tasks.AndroidTestTask
import com.gradle.develocity.agent.gradle.test.DevelocityTestConfiguration
import com.gradle.develocity.agent.gradle.test.TestRetryConfiguration
import gradle.api.project.execute
import gradle.api.project.registerAggregationTestTask
import klib.data.type.primitives.string.uppercaseFirstChar
import org.gradle.api.Project
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.api.tasks.testing.Test
import org.gradle.internal.extensions.core.extra
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmWasiTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest
import org.jetbrains.kotlin.gradle.tasks.KotlinTest
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.KonanTarget

public sealed class CI {

    public val name: String = this::class.simpleName!!

    context(_: Project)
    public abstract val branch: String

    context(_: Project)
    public abstract val run: String?

    public abstract var dependenciesCheck: Boolean

    public abstract var formatCheck: Boolean

    public abstract var qualityCheck: Boolean

    public abstract var coverageVerify: Boolean

    public abstract var docSamplesCheck: Boolean

    public abstract var test: Boolean

    public val publishRepositories: MutableSet<PublishRepository> = linkedSetOf()

    private val versioning: Versioning = Versioning()

    public fun versioning(block: Versioning.() -> Unit): Unit = versioning.run(block)

    context(project: Project)
    public val buildMetadata: String
        get() = "${branch.takeIf { versioning.branch }.orEmpty()}${run?.takeIf { versioning.run }.orEmpty()}"

    public class Github internal constructor(
        override var dependenciesCheck: Boolean = true,
        override var formatCheck: Boolean = true,
        override var qualityCheck: Boolean = true,
        override var coverageVerify: Boolean = true,
        override var docSamplesCheck: Boolean = true,
        override var test: Boolean = true,
    ) : CI() {

        context(_: Project)
        override val branch: String
            get() = ref

        context(_: Project)
        override val run: String?
            get() = runNumber

        public companion object {

            public const val KEY: String = "GITHUB_ACTION"

            // The GITHUB_REF_NAME provide the reference name.
            public val ref: String
                get() = System.getenv("GITHUB_REF_NAME") ?: "unknown"

            // The GITHUB_RUN_NUMBER A unique number for each run of a particular workflow in a repository.
            // This number begins at 1 for the workflow's first run, and increments with each new run.
            // This number does not change if you re-run the workflow run.
            public val runNumber: String?
                get() = System.getenv("GITHUB_RUN_NUMBER")
        }
    }

    public class TeamCity internal constructor(
        override var dependenciesCheck: Boolean = true,
        override var formatCheck: Boolean = true,
        override var qualityCheck: Boolean = true,
        override var coverageVerify: Boolean = true,
        override var docSamplesCheck: Boolean = true,
        override var test: Boolean = true,
    ) : CI() {

        context(project: Project)
        override val branch: String
            get() = gitBranch

        context(_: Project)
        override val run: String?
            get() = buildNumber

        public companion object {

            public const val KEY: String = "TEAMCITY_VERSION"

            context(project: Project)
            public val gitBranch: String
                get() = project.execute(" echo %teamcity.build.branch%")

            public val buildNumber: String?
                get() = System.getenv("BUILD_NUMBER")

            context(project: Project)
            public val buildId: String?
                get() = project.property("teamcity.build.id")?.toString()

            context(project: Project)
            public val buildTypeId: String?
                get() = project.property("teamcity.buildType.id")?.toString()
        }
    }

    public companion object {

        private val github = Github()

        private val teamCity = TeamCity()

        public val current: CI?
            get() = when {
                System.getenv().contains(Github.KEY) -> github
                System.getenv().contains(TeamCity.KEY) -> teamCity
                else -> null
            }

        public fun github(block: Github.() -> Unit): Unit = github.run(block)

        public fun teamCity(block: TeamCity.() -> Unit): Unit = teamCity.run(block)

        context(project: Project)
        internal fun configureTasks(): Unit = with(project) {
            val ci = current ?: return@with

            pluginManager.withPlugin("org.owasp.dependencycheck") {
                tasks.register(ci.name) {
                    dependsOn(tasks.named("dependencyCheckAnalyze"))
                    onlyIf { ci.dependenciesCheck }
                }
            }

            pluginManager.withPlugin("com.diffplug.spotless") {
                tasks.register(ci.name) {
                    dependsOn(tasks.named("spotlessCheck"))
                    onlyIf { ci.formatCheck }
                }
            }

            pluginManager.withPlugin("org.sonarqube") {
                tasks.register(ci.name) {
                    dependsOn(tasks.named("sonar"))
                    onlyIf { ci.qualityCheck }
                }
            }

            tasks.register(ci.name) {
                dependsOn(tasks.named("check"))
                onlyIf { ci.test }
            }

            pluginManager.withPlugin("org.jetbrains.kotlinx.kover") {
                tasks.register(ci.name) {
                    dependsOn(tasks.named("koverVerify"))
                    onlyIf { ci.coverageVerify }
                }
            }

            pluginManager.withPlugin("org.jetbrains.kotlinx.knit") {
                tasks.register(ci.name) {
                    dependsOn(tasks.named("knitCheck"))
                    onlyIf { ci.docSamplesCheck }
                }
            }

            pluginManager.withPlugin("maven-publish") {
                ci.publishRepositories.forEach { (name, enabled) ->
                    val publishTaskName = "publishAllPublicationsTo${name.uppercaseFirstChar()}Repository"
                    tasks.register("${ci.name}${publishTaskName.uppercaseFirstChar()}") {
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
                testRetry {
                    maxRetries = 1
                    maxFailures = 10
                }

                applyTestRetryCompatibilityWorkaround()
            }

            registerAggregationTestTasks()
        }

        private fun Test.testRetry(configure: TestRetryConfiguration.() -> Unit) {
            (extensions.findByName("develocity") as DevelocityTestConfiguration?)?.testRetry(configure)
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

        private fun Project.registerAggregationTestTasks() =
            pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                registerAggregationTestTask<KotlinJvmTest>(
                    "jvmAll",
                    { target -> target is KotlinJvmTarget },
                )

                // test only on min and max JDK versions
                registerAggregationTestTask<KotlinJvmTest>(
                    "jvmJdkRange",
                    { target -> target is KotlinJvmTarget },
                ) {
                    it.matching { spec -> spec.javaLauncher.get().metadata.languageVersion.asInt() in setOf(8, 23) }
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
                registerAggregationTestTask<KotlinNativeTest>(
                    "androidNativeArm32All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.ANDROID_ARM32
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "androidNativeX86All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.ANDROID_X86
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }

                val androidNative32Targets = listOf(KonanTarget.ANDROID_X86, KonanTarget.ANDROID_ARM32)

                registerAggregationTestTask<KotlinNativeTest>(
                    "androidNative32",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget in androidNative32Targets
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "androidNativeArm64All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.ANDROID_ARM64
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "androidNativeX64All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.ANDROID_X64
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }

                val androidNative64Targets = listOf(
                    KonanTarget.ANDROID_ARM64, KonanTarget.ANDROID_X64,
                )

                registerAggregationTestTask<KotlinNativeTest>(
                    "androidNative64",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget in androidNative64Targets
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "androidNative",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget.family == Family.ANDROID
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }

                // Darwin
                // ios
                registerAggregationTestTask<KotlinNativeTest>(
                    "iosArm64All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.IOS_ARM64
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "iosX64All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.IOS_X64
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "iosSimulatorArm64All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.IOS_SIMULATOR_ARM64
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "ios",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget.family == Family.IOS
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }

                // watchos
                registerAggregationTestTask<KotlinNativeTest>(
                    "watchosArm32All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.WATCHOS_ARM32
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "watchosArm64All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.WATCHOS_ARM64
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "watchosX64All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.WATCHOS_X64
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }

                val watchos64Targets = listOf(KonanTarget.WATCHOS_X64, KonanTarget.WATCHOS_ARM64)

                registerAggregationTestTask<KotlinNativeTest>(
                    "watchosDeviceArm64All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.WATCHOS_DEVICE_ARM64
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "watchos64",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget in watchos64Targets
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "watchosSimulatorArm64All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.WATCHOS_SIMULATOR_ARM64
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "watchos",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget.family == Family.WATCHOS
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                // tvos
                registerAggregationTestTask<KotlinNativeTest>(
                    "tvosArm64All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.TVOS_ARM64
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "tvosX64All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.TVOS_X64
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "tvosSimulatorArm64All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.TVOS_SIMULATOR_ARM64
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "tvos",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget.family == Family.TVOS
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                // macos
                registerAggregationTestTask<KotlinNativeTest>(
                    "macosArm64All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.MACOS_ARM64
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "macosX64All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.MACOS_X64
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "macos",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget.family == Family.OSX
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                // apple
                val appleTargets = listOf(Family.IOS, Family.WATCHOS, Family.TVOS, Family.OSX)

                registerAggregationTestTask<KotlinNativeTest>(
                    "apple",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget.family in appleTargets
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }

                // Linux
                registerAggregationTestTask<KotlinNativeTest>(
                    "linuxArm64All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.LINUX_ARM64
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "linuxX64All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.LINUX_X64
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }
                registerAggregationTestTask<KotlinNativeTest>(
                    "linux",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget.family == Family.LINUX
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }

                // Windows
                registerAggregationTestTask<KotlinNativeTest>(
                    "mingwX64All",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget == KonanTarget.MINGW_X64
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }

                registerAggregationTestTask<KotlinNativeTest>(
                    "mingwAll",
                    { target ->
                        target is KotlinNativeTarget && target.konanTarget.family == Family.MINGW
                    },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }

                // Native
                registerAggregationTestTask<KotlinNativeTest>(
                    "nativeAll",
                    { it is KotlinNativeTarget },
                ) {
                    it.matching(KotlinNativeTest::isEnabled)
                }

                // Web
                registerAggregationTestTask<KotlinJsTest>(
                    "jsAll",
                    { target ->
                        target is KotlinJsTargetDsl && target.platformType == KotlinPlatformType.js
                    },
                )
                registerAggregationTestTask<KotlinJsTest>(
                    "wasmAll",
                    { target -> target is KotlinWasmTargetDsl },
                )
                registerAggregationTestTask<KotlinJsTest>(
                    "jsAndWasmAll",
                    { target -> target is KotlinJsTargetDsl },
                )
                registerAggregationTestTask<KotlinJsTest>(
                    "wasmWasiAll",
                    { target -> target is KotlinWasmWasiTargetDsl },
                )
                registerAggregationTestTask<KotlinJsTest>(
                    "wasmWasiAll",
                    { target -> target is KotlinWasmTargetDsl },
                )
            }
    }
}
