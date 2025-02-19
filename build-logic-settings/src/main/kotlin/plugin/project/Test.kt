package plugin.project

import com.gradle.develocity.agent.gradle.test.DevelocityTestConfiguration
import com.gradle.develocity.agent.gradle.test.TestRetryConfiguration
import gradle.hasNative
import gradle.isCI
import gradle.kotlin
import gradle.maybeNamed
import org.gradle.api.Project
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.internal.extensions.core.extra
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
import org.jetbrains.kotlin.gradle.tasks.KotlinTest
//import plugin.project.jvm.configureKotlinJvmTest

internal fun Project.configureTest() {
    if (isCI) {
        configureTestOnCI()
    }
//    configureKotlinJvmTest()
    configureTestLogging()
}

private fun Project.configureTestLogging() = tasks.withType<Test> {
    testLogging {
        events(
            TestLogEvent.FAILED,
            TestLogEvent.STANDARD_ERROR,
            TestLogEvent.SKIPPED,
            TestLogEvent.STANDARD_OUT,
        )
        exceptionFormat = TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }

    val failedTests = mutableListOf<Pair<TestDescriptor, StackTraceElement?>>()

    // Add listener to log test results
    addTestListener(
        object : TestListener {
            override fun beforeSuite(suite: TestDescriptor) {
                // Nothing to do
            }

            override fun beforeTest(testDescriptor: TestDescriptor) {
                // Nothing to do
            }

            override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
                when (result.resultType) {
                    TestResult.ResultType.FAILURE -> {
                        val relevantStackTrace =
                            result.exception?.stackTrace?.last { it.className == testDescriptor.className }
                        failedTests.add(Pair(testDescriptor, relevantStackTrace))
                    }

                    else -> {}
                }
            }

            override fun afterSuite(suite: TestDescriptor, result: TestResult) {
                if (suite.parent == null) { // root suite
                    if (failedTests.isNotEmpty()) {
                        logger.lifecycle("\tFailed Tests:")
                        failedTests.forEach {
                            val (testDescriptor, relevantStackTrace) = it
                            val file = testDescriptor.className?.split(".")?.last()
                            val fqcn = testDescriptor.className
                            val testName = testDescriptor.name

                            val intelliJFormatWithFileLink =
                                if (relevantStackTrace != null) "at $relevantStackTrace" else "at $fqcn.$testName($file)"

                            parent?.let { parent ->
                                logger.lifecycle("\t\t${parent.name} - $intelliJFormatWithFileLink")
                            } ?: logger.lifecycle("\t\t$intelliJFormatWithFileLink")
                        }
                    }
                }
            }
        },
    )
}

/** Applies CI-specific configurations to test tasks. */
private fun Project.configureTestOnCI() {
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

    registerAggregationTestTask(
        name = "jvmAllTest",
        taskDependencies = { tasks.withType<KotlinJvmTest>() },
        targetFilter = { it.platformType == KotlinPlatformType.jvm },
    )

    registerAggregationTestTask(
        name = "nativeTest",
        taskDependencies = { tasks.withType<KotlinNativeTest>().matching { it.enabled } },
        targetFilter = { it.platformType == KotlinPlatformType.native },
    )

    listOf("ios", "watchos", "tvos", "macos").forEach { targetGroup ->
        registerAggregationTestTask(
            name = "${targetGroup}Test",
            taskDependencies = {
                tasks.withType<KotlinNativeTest>().matching {
                    it.enabled && it.name.startsWith(targetGroup, ignoreCase = true)
                }
            },
            targetFilter = {
                it.platformType == KotlinPlatformType.native && it.name.startsWith(targetGroup, ignoreCase = true)
            },
        )
    }

    tasks.register("linkAll") {
        dependsOn(tasks.withType<KotlinNativeLink>())
    }

    if (providers.gradleProperty("project.skipTestTasks").map(String::toBoolean).getOrElse(false)) {
        tasks.withType<AbstractTestTask>().configureEach { onlyIf { false } }
    }

    if (providers.gradleProperty("project.skipLinkTasks").map(String::toBoolean).getOrElse(false)) {
        tasks.withType<KotlinNativeLink>().configureEach { onlyIf { false } }
    }

    val os = OperatingSystem.current()

    // Run native tests only on matching host.
    // There is no need to configure `onlyIf` for Darwin targets as they're configured by KGP.
    @Suppress("UnstableApiUsage")
    if (kotlin.targets.hasNative()) {
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

// Docs: https://docs.gradle.com/develocity/gradle-plugin/current/#test_retry
private fun Test.testRetry(configure: TestRetryConfiguration.() -> Unit) {
    extensions.getByName<DevelocityTestConfiguration>("develocity").testRetry(configure)
}

private fun Project.registerAggregationTestTask(
    name: String,
    taskDependencies: () -> TaskCollection<*>,
    targetFilter: (KotlinTarget) -> Boolean,
) {
    extensions.configure<KotlinMultiplatformExtension>("kotlin") {
        var called = false
        targets.matching(targetFilter).configureEach {
            if (called) return@configureEach
            called = true

            tasks.register(name) {
                group = "verification"
                dependsOn(taskDependencies())
            }
        }
    }
}
