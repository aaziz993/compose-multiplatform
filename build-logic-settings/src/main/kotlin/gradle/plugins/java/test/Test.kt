package gradle.plugins.java.test

import gradle.accessors.javaToolchain

import gradle.api.tasks.util.PatternFilterable
import gradle.api.tasks.test.AbstractTestTask
import gradle.api.tasks.test.DefaultTestFilter
import gradle.api.tasks.test.TestLoggingContainer
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import gradle.plugins.java.test.JavaForkOptions
import gradle.plugins.java.JavaToolchainSpec
import gradle.plugins.java.ModularitySpec
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * Executes JUnit (3.8.x, 4.x or 5.x) or TestNG tests. Test are always run in (one or more) separate JVMs.
 *
 *
 *
 * The sample below shows various configuration options.
 *
 * <pre class='autoTested'>
 * plugins {
 * id("java-library") // adds 'test' task
 * }
 *
 * test {
 * // discover and execute JUnit4-based tests
 * useJUnit()
 *
 * // discover and execute TestNG-based tests
 * useTestNG()
 *
 * // discover and execute JUnit Platform-based tests
 * useJUnitPlatform()
 *
 * // set a system property for the test JVM(s)
 * systemProperty 'some.prop', 'value'
 *
 * // explicitly include or exclude tests
 * include 'org/foo/ **'
 * exclude 'org/boo/ **'
 *
 * // show standard out and standard error of the test JVM(s) on the console
 * testLogging.showStandardStreams = true
 *
 * // set heap size for the test JVM(s)
 * minHeapSize = "128m"
 * maxHeapSize = "512m"
 *
 * // set JVM arguments for the test JVM(s)
 * jvmArgs('-XX:MaxPermSize=256m')
 *
 * // listen to events in the test execution lifecycle
 * beforeTest { descriptor -&gt;
 * logger.lifecycle("Running test: " + descriptor)
 * }
 *
 * // fail the 'test' task on the first test failure
 * failFast = true
 *
 * // skip an actual test execution
 * dryRun = true
 *
 * // listen to standard out and standard error of the test JVM(s)
 * onOutput { descriptor, event -&gt;
 * logger.lifecycle("Test: " + descriptor + " produced standard out/err: " + event.message )
 * }
 * }
</pre> *
 *
 *
 * The test process can be started in debug mode (see [.getDebug]) in an ad-hoc manner by supplying the `--debug-jvm` switch when invoking the build.
 * <pre>
 * gradle someTestTask --debug-jvm
</pre> *
 */
internal abstract class Test : AbstractTestTask(), JavaForkOptions, PatternFilterable {

    /**
     * Indicates if this task will skip individual test execution.
     *
     *
     *
     * For JUnit 4 and 5, this will report tests that would have executed as skipped.
     * For TestNG, this will report tests that would have executed as passed.
     *
     *
     *
     *
     * Only versions of TestNG which support native dry-running are supported, i.e. TestNG 6.14 or later.
     *
     *
     * @return property for whether this task will skip individual test execution
     * @since 8.3
     */
    abstract val dryRun: Boolean?

    /**
     * Returns the module path handling of this test task.
     *
     * @since 6.4
     */
    abstract val modularity: ModularitySpec?

    /**
     * Sets the directories to scan for compiled test sources.
     *
     * Typically, this would be configured to use the output of a source set:
     * <pre class='autoTested'>
     * plugins {
     * id 'java'
     * }
     *
     * sourceSets {
     * integrationTest {
     * compileClasspath += main.output
     * runtimeClasspath += main.output
     * }
     * }
     *
     * task integrationTest(type: Test) {
     * // Runs tests from src/integrationTest
     * testClassesDirs = sourceSets.integrationTest.output.classesDirs
     * classpath = sourceSets.integrationTest.runtimeClasspath
     * }
    </pre> *
     *
     * @param testClassesDirs All test class directories to be used.
     * @since 4.0
     */
    abstract val testClassesDirs: List<String>?

    /**
     * Specifies that JUnit4 should be used to discover and execute the tests.
     *
     * @see .useJUnit
     */
    abstract val useJUnit: Boolean?

    /**
     * Specifies that JUnit4 should be used to discover and execute the tests with additional configuration.
     *
     *
     * The supplied action configures an instance of [JUnit4 specific options][JUnitOptions].
     *
     * @param testFrameworkConfigure An action used to configure JUnit4 options.
     * @since 3.5
     */
    abstract val useJUnitDsl: JUnitOptions?

    /**
     * Specifies that JUnit Platform should be used to discover and execute the tests.
     *
     *
     * Use this option if your tests use JUnit Jupiter/JUnit5.
     *
     *
     * JUnit Platform supports multiple test engines, which allows other testing frameworks to be built on top of it.
     * You may need to use this option even if you are not using JUnit directly.
     *
     * @see .useJUnitPlatform
     * @since 4.6
     */
    abstract val useJUnitPlatform: Boolean?

    /**
     * Specifies that JUnit Platform should be used to discover and execute the tests with additional configuration.
     *
     *
     * Use this option if your tests use JUnit Jupiter/JUnit5.
     *
     *
     * JUnit Platform supports multiple test engines, which allows other testing frameworks to be built on top of it.
     * You may need to use this option even if you are not using JUnit directly.
     *
     *
     * The supplied action configures an instance of [JUnit Platform specific options][JUnitPlatformOptions].
     *
     * @param testFrameworkConfigure A closure used to configure JUnit platform options.
     * @since 4.6
     */
    abstract val useJUnitPlatformDsl: JUnitPlatformOptions?

    /**
     * Specifies that TestNG should be used to discover and execute the tests.
     *
     * @see .useTestNG
     */
    abstract val useTestNG: Boolean?

    /**
     * Specifies that TestNG should be used to discover and execute the tests with additional configuration.
     *
     *
     * The supplied action configures an instance of [TestNG specific options][TestNGOptions].
     *
     * @param testFrameworkConfigure An action used to configure TestNG options.
     * @since 3.5
     */
    abstract val useTestNGDsl: TestNGOptions?

    abstract val scanForTestClasses: Boolean?

    /**
     * Sets the maximum number of test classes to execute in a forked test process.
     *
     *
     * By default, Gradle automatically uses a separate JVM when executing tests, so changing this property is usually not necessary.
     *
     *
     * @param forkEvery The maximum number of test classes. Use 0 to specify no maximum.
     * @since 8.1
     */
    abstract val forkEvery: Long?

    /**
     * Sets the maximum number of test processes to start in parallel.
     *
     *
     * By default, Gradle executes a single test class at a time but allows multiple [Test] tasks to run in parallel.
     *
     *
     * @param maxParallelForks The maximum number of forked test processes. Use 1 to disable parallel test execution for this task.
     */
    abstract val maxParallelForks: Int?

    abstract val javaLauncher: JavaToolchainSpec?

        context(Project)
    override fun applyTo(recipient: T) {
        super<AbstractTestTask>.applyTo(named)

        named as org.gradle.api.tasks.testing.Test

        super<JavaForkOptions>.applyTo(named)
        super<PatternFilterable>.applyTo(named)

        named.dryRun tryAssign dryRun
        modularity?.applyTo(named.modularity)
        testClassesDirs?.toTypedArray()?.let(::files)?.let(named::setTestClassesDirs)
        useJUnit?.takeIf { it }?.run { named.useJUnit() }

        useJUnitDsl?.let { useJUnitDsl ->
            named.useJUnit {
                useJUnitDsl.applyTo(this)
            }
        }

        useJUnitPlatform?.takeIf { it }?.run { named.useJUnitPlatform() }

        useJUnitPlatformDsl?.let { useJUnitPlatformDsl ->
            named.useJUnitPlatform {
                useJUnitPlatformDsl.applyTo(this)
            }
        }

        useTestNG?.takeIf { it }?.run { named.useTestNG() }

        useTestNGDsl?.let { useTestNGDsl ->
            named.useTestNG {
                useTestNGDsl.applyTo(this)
            }
        }

        scanForTestClasses?.let(named::setScanForTestClasses)
        forkEvery?.let(named::setForkEvery)
        named.maxParallelForks = maxParallelForks ?: (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)

        javaLauncher?.let { javaLauncher ->
            named.javaLauncher = javaToolchain.launcherFor {
                javaLauncher.applyTo(this)
            }
        }
    }

    context(Project)
    override fun applyTo() =
        super<AbstractTestTask>.applyTo(tasks.withType<org.gradle.api.tasks.testing.Test>())
}

/** Configure tests against different JDK versions. */
internal fun org.gradle.api.tasks.testing.Test.configureJavaToolchain() =
    javaLauncher.get().metadata.languageVersion.asInt().let { languageVersion ->

        if (languageVersion >= 16) {
            // Allow reflective access from tests
            jvmArgs(
                "--add-opens=java.base/java.net=ALL-UNNAMED",
                "--add-opens=java.base/java.time=ALL-UNNAMED",
                "--add-opens=java.base/java.util=ALL-UNNAMED",
            )
        }

        if (languageVersion >= 21) {
            // coroutines-debug use dynamic agent loading under the hood.
            // Remove as soon as the issue is fixed: https://youtrack.jetbrains.com/issue/KT-62096/
            jvmArgs("-XX:+EnableDynamicAgentLoading")
        }
    }

@Serializable
@SerialName("Test")
internal data class TestImpl(
    override val dryRun: Boolean? = null,
    override val modularity: ModularitySpec? = null,
    override val testClassesDirs: List<String>? = null,
    override val useJUnit: Boolean? = null,
    override val useJUnitDsl: JUnitOptions? = null,
    override val useJUnitPlatform: Boolean? = null,
    override val useJUnitPlatformDsl: JUnitPlatformOptions? = null,
    override val useTestNG: Boolean? = null,
    override val useTestNGDsl: TestNGOptions? = null,
    override val scanForTestClasses: Boolean? = null,
    override val forkEvery: Long? = null,
    override val maxParallelForks: Int? = null,
    override val binaryResultsDirectory: String? = null,
    override val ignoreFailures: Boolean? = null,
    override val testLogging: TestLoggingContainer? = null,
    override val testNameIncludePatterns: List<String>? = null,
    override val failFast: Boolean? = null,
    override val filter: DefaultTestFilter? = null,
    override val dependsOn: LinkedHashSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: LinkedHashSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String? = null,,
    override val systemProperties: SerializableAnyMap? = null,
    override val setSystemProperties: SerializableAnyMap? = null,
    override val defaultCharacterEncoding: String? = null,
    override val minHeapSize: String? = null,
    override val maxHeapSize: String? = null,
    override val jvmArgs: List<String>? = null,
    override val setJvmArgs: List<String>? = null,
    override val bootstrapClasspath: List<String>? = null,
    override val setBootstrapClasspath: List<String>? = null,
    override val enableAssertions: Boolean? = null,
    override val debug: Boolean? = null,
    override val debugOptions: JavaDebugOptions? = null,
    override val allJvmArgs: List<String>? = null,
    override val executable: String? = null,
    override val workingDir: String? = null,
    override val environment: SerializableAnyMap? = null,
    override val setEnvironment: SerializableAnyMap? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
    override val javaLauncher: JavaToolchainSpec? = null,
) : Test()
