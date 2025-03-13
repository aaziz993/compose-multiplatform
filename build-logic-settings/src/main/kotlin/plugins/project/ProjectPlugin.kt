package plugins.project

import com.gradle.develocity.agent.gradle.test.DevelocityTestConfiguration
import com.gradle.develocity.agent.gradle.test.TestRetryConfiguration
import gradle.accessors.exportExtras
import gradle.accessors.kotlin
import org.gradle.kotlin.dsl.withType
import org.gradle.kotlin.dsl.register
import gradle.accessors.projectProperties
import gradle.api.CI
import gradle.api.maybeNamed
import gradle.api.trySetSystemProperty
import gradle.api.version
import gradle.plugins.project.PROJECT_PROPERTIES_FILE
import gradle.plugins.project.ProjectProperties.Companion.load
import gradle.plugins.project.ProjectProperties.Companion.yaml
import gradle.serialization.encodeToAny
import javax.xml.stream.XMLEventFactory
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory
import kotlinx.serialization.json.Json
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.api.tasks.testing.Test
import org.gradle.internal.extensions.core.extra
import org.gradle.internal.os.OperatingSystem
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
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
import plugins.doctor.DoctorPlugin
import plugins.dokka.DokkaPlugin
import plugins.initialization.problemreporter.SLF4JProblemReporterContext
import plugins.java.JavaPlugin
import plugins.kmp.KMPPlugin
import plugins.knit.KnitPlugin
import plugins.kotlin.allopen.AllOpenPlugin
import plugins.kotlin.apollo.ApolloPlugin
import plugins.kotlin.atomicfu.AtomicFUPlugin
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
import plugins.sonar.SonarPlugin
import plugins.spotless.SpotlessPlugin
import plugins.web.JsPlugin
import plugins.web.WasmPlugin
import plugins.web.WasmWasiPlugin

public class ProjectPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(SLF4JProblemReporterContext()) {
        with(target) {
            // Load and apply project.yaml to build.gradle.kts properties.
            projectProperties = layout.projectDirectory.load().also { properties ->
                println("Load and apply $PROJECT_PROPERTIES_FILE to: $name")
                println(yaml.dump(Json.Default.encodeToAny(properties)))
            }

            exportExtras()

            if (projectProperties.kotlin.targets.isNotEmpty()) {
                projectProperties.group?.let(::setGroup)
                projectProperties.description?.let(::setDescription)
                project.version = version()
            }

            projectProperties.buildscript?.applyTo()

            //  Don't change order!
            project.plugins.apply(DoctorPlugin::class.java)
            project.plugins.apply(BuildConfigPlugin::class.java)
            project.plugins.apply(SpotlessPlugin::class.java)
            project.plugins.apply(KoverPlugin::class.java)
            project.plugins.apply(SonarPlugin::class.java)
            project.plugins.apply(KnitPlugin::class.java)
            project.plugins.apply(DokkaPlugin::class.java)
            project.plugins.apply(ShadowPlugin::class.java)
            project.plugins.apply(ApiValidationPlugin::class.java)
            project.plugins.apply(PublishPlugin::class.java)
            project.plugins.apply(AllOpenPlugin::class.java)
            project.plugins.apply(NoArgPlugin::class.java)
            project.plugins.apply(AtomicFUPlugin::class.java)
            project.plugins.apply(SerializationPlugin::class.java)
            project.plugins.apply(SqlDelightPlugin::class.java)
            project.plugins.apply(RoomPlugin::class.java)
            project.plugins.apply(RpcPlugin::class.java)
            project.plugins.apply(KtorfitPlugin::class.java)
            project.plugins.apply(ApolloPlugin::class.java)
            project.plugins.apply(PowerAssertPlugin::class.java)
            project.plugins.apply(JavaPlugin::class.java) // should be applied before kmp plugin.
            project.plugins.apply(AndroidPlugin::class.java) // apply and configure android library or application plugin.
            project.plugins.apply(AnimalSnifferPlugin::class.java)
            project.plugins.apply(KMPPlugin::class.java) // need android library or application plugin applied.
            project.plugins.apply(KspPlugin::class.java) // kspCommonMainMetadata need kmp plugin applied.
            project.plugins.apply(NativePlugin::class.java)
            project.plugins.apply(ApplePlugin::class.java)
            project.plugins.apply(JsPlugin::class.java)
            project.plugins.apply(WasmPlugin::class.java)
            project.plugins.apply(WasmWasiPlugin::class.java)
            project.plugins.apply(CMPPlugin::class.java)

            projectProperties.nodeJsEnv.applyTo()
            projectProperties.yarn.applyTo()
            projectProperties.npm.applyTo()

            afterEvaluate {
                // W/A for XML factories mess within apple plugin classpath.
                val hasAndroidPlugin = plugins.hasPlugin("com.android.application") ||
                    plugins.hasPlugin("com.android.library")
                if (hasAndroidPlugin) {
                    adjustXmlFactories()
                }
            }

            projectProperties.dependencies?.forEach { dependency ->
                dependencies {
                    dependency.applyTo(this)
                }
            }

            if (CI) {
                configureTestOnCI()
            }

            projectProperties.tasks?.forEach { task ->
                task.applyTo()
            }

            if (problemReporter.getErrors().isNotEmpty()) {
                throw GradleException(problemReporter.getGradleError())
            }
        }
    }

    /**
     * W/A for service loading conflict between apple plugin
     * and android plugin.
     */
    private fun adjustXmlFactories() {
        trySetSystemProperty(
            XMLInputFactory::class.qualifiedName!!,
            "com.sun.xml.internal.stream.XMLInputFactoryImpl",
        )
        trySetSystemProperty(
            XMLOutputFactory::class.qualifiedName!!,
            "com.sun.xml.internal.stream.XMLOutputFactoryImpl",
        )
        trySetSystemProperty(
            XMLEventFactory::class.qualifiedName!!,
            "com.sun.xml.internal.stream.events.XMLEventFactoryImpl",
        )
    }

    /** Applies CI-specific configurations to test tasks. */
    @Suppress("UnstableApiUsage")
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
        kotlin {
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
}
