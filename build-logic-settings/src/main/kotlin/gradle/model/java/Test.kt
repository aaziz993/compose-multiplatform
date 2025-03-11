package gradle.model.java

import groovy.lang.Closure
import groovy.lang.DelegatesTo
import org.gradle.StartParameter
import org.gradle.api.*
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileTreeElement
import org.gradle.api.internal.DocumentationRegistry
import org.gradle.api.internal.classpath.ModuleRegistry
import org.gradle.api.internal.tasks.testing.JvmTestExecutionSpec
import org.gradle.api.internal.tasks.testing.TestExecutableUtils
import org.gradle.api.internal.tasks.testing.TestExecuter
import org.gradle.api.internal.tasks.testing.TestFramework
import org.gradle.api.internal.tasks.testing.detection.DefaultTestExecuter
import org.gradle.api.internal.tasks.testing.filter.DefaultTestFilter
import org.gradle.api.internal.tasks.testing.junit.JUnitTestFramework
import org.gradle.api.internal.tasks.testing.junit.result.TestClassResult
import org.gradle.api.internal.tasks.testing.junit.result.TestResultSerializer
import org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestFramework
import org.gradle.api.internal.tasks.testing.testng.TestNGTestFramework
import org.gradle.api.internal.tasks.testing.worker.TestWorker
import org.gradle.api.jvm.ModularitySpec
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.specs.Spec
import org.gradle.api.tasks.*
import org.gradle.api.tasks.options.Option
import org.gradle.api.tasks.testing.junit.JUnitOptions
import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions
import org.gradle.api.tasks.testing.testng.TestNGOptions
import org.gradle.api.tasks.util.PatternFilterable
import org.gradle.api.tasks.util.PatternSet
import org.gradle.internal.Actions
import org.gradle.internal.Cast
import org.gradle.internal.Factory
import org.gradle.internal.actor.ActorFactory
import org.gradle.internal.concurrent.CompositeStoppable
import org.gradle.internal.deprecation.DeprecationLogger
import org.gradle.internal.instrumentation.api.annotations.ToBeReplacedByLazyProperty
import org.gradle.internal.jvm.DefaultModularitySpec
import org.gradle.internal.jvm.JavaModuleDetector
import org.gradle.internal.jvm.UnsupportedJavaRuntimeException
import org.gradle.internal.scan.UsedByScanPlugin
import org.gradle.internal.time.Clock
import org.gradle.internal.work.WorkerLeaseService
import org.gradle.jvm.toolchain.JavaLauncher
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.jvm.toolchain.JavaToolchainSpec
import org.gradle.jvm.toolchain.internal.JavaExecutableUtils
import org.gradle.process.CommandLineArgumentProvider
import org.gradle.process.JavaDebugOptions
import org.gradle.process.JavaForkOptions
import org.gradle.process.ProcessForkOptions
import org.gradle.process.internal.JavaForkOptionsFactory
import org.gradle.process.internal.worker.WorkerProcessFactory
import org.gradle.util.internal.ConfigureUtil
import java.io.File
import java.util.concurrent.Callable
import javax.inject.Inject

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
    private val forkOptions: JavaForkOptions
    private val modularity: ModularitySpec
        /**
         * Returns the module path handling of this test task.
         *
         * @since 6.4
         */
        @Nested get() {
            return field
        }
    private val javaLauncher: Property<JavaLauncher?>

    private var testClassesDirs: FileCollection? = null
    private val patternSet: PatternFilterable?
    private var classpath: FileCollection
    private val stableClasspath: ConfigurableFileCollection
    private val testFramework: Property<TestFramework>
    private var scanForTestClasses = true
    private var forkEvery: Long = 0
    private var maxParallelForks = 1
    private var testExecuter: TestExecuter<JvmTestExecutionSpec?>? = null

    init {
        val objectFactory = getObjectFactory()
        patternSet = getPatternSetFactory()!!.create()
        classpath = objectFactory.fileCollection()
        // Create a stable instance to represent the classpath, that takes care of conventions and mutations applied to the property
        stableClasspath = objectFactory.fileCollection()
        stableClasspath.from(object : Callable<Any?> {
            override fun call(): Any {
                return getClasspath()
            }
        })
        forkOptions = getForkOptionsFactory()!!.newDecoratedJavaForkOptions()
        forkOptions.setEnableAssertions(true)
        forkOptions.setExecutable(null)
        modularity = objectFactory.newInstance<DefaultModularitySpec>(DefaultModularitySpec::class.java)
        javaLauncher =
            objectFactory.property<JavaLauncher?>(JavaLauncher::class.java).convention(createJavaLauncherConvention())
        javaLauncher.finalizeValueOnRead()
        getDryRun()!!.convention(false)
        testFramework = objectFactory.property<TestFramework?>(TestFramework::class.java)
            .convention(JUnitTestFramework(this, getFilter() as DefaultTestFilter?, true))
    }

    private fun createJavaLauncherConvention(): Provider<JavaLauncher?> {
        val objectFactory = getObjectFactory()
        val javaToolchainService = getJavaToolchainService()
        val executableOverrideToolchainSpec =
            getProviderFactory()!!.provider<JavaToolchainSpec?>(object : Callable<JavaToolchainSpec?> {
                override fun call(): JavaToolchainSpec {
                    return TestExecutableUtils.getExecutableToolchainSpec(this@Test, objectFactory)!!
                }
            })

        return executableOverrideToolchainSpec
            .flatMap<JavaLauncher?>(object : Transformer<Provider<JavaLauncher?>?, JavaToolchainSpec?> {
                override fun transform(spec: JavaToolchainSpec): Provider<JavaLauncher?> {
                    return javaToolchainService.launcherFor(spec)
                }
            })
            .orElse(javaToolchainService.launcherFor(object : Action<JavaToolchainSpec?> {
                override fun execute(javaToolchainSpec: JavaToolchainSpec) {}
            }))
    }

    /**
     * {@inheritDoc}
     */
    @Internal
    @ToBeReplacedByLazyProperty
    override fun getWorkingDir(): File {
        return forkOptions.getWorkingDir()
    }

    /**
     * {@inheritDoc}
     */
    override fun setWorkingDir(dir: File) {
        forkOptions.setWorkingDir(dir)
    }

    /**
     * {@inheritDoc}
     */
    override fun setWorkingDir(dir: Any) {
        forkOptions.setWorkingDir(dir)
    }

    /**
     * {@inheritDoc}
     */
    override fun workingDir(dir: Any): Test {
        forkOptions.workingDir(dir)
        return this
    }

    /**
     * Returns the version of Java used to run the tests based on the [JavaLauncher] specified by [.getJavaLauncher],
     * or the executable specified by [.getExecutable] if the `JavaLauncher` is not present.
     *
     * @since 3.3
     */
    @Input
    @ToBeReplacedByLazyProperty
    public fun getJavaVersion(): JavaVersion {
        return JavaVersion.toVersion(getJavaLauncher().get().getMetadata().getLanguageVersion().asInt())
    }

    /**
     * {@inheritDoc}
     */
    @Internal
    @ToBeReplacedByLazyProperty
    override fun getExecutable(): String {
        return forkOptions.getExecutable()
    }

    /**
     * {@inheritDoc}
     */
    override fun executable(executable: Any): Test {
        forkOptions.executable(executable)
        return this
    }

    /**
     * {@inheritDoc}
     */
    override fun setExecutable(executable: String) {
        forkOptions.setExecutable(executable)
    }

    /**
     * {@inheritDoc}
     */
    override fun setExecutable(executable: Any) {
        forkOptions.setExecutable(executable)
    }

    /**
     * {@inheritDoc}
     */
    @ToBeReplacedByLazyProperty
    override fun getSystemProperties(): MutableMap<String?, Any?> {
        return forkOptions.getSystemProperties()
    }

    /**
     * {@inheritDoc}
     */
    override fun setSystemProperties(properties: MutableMap<String?, *>) {
        forkOptions.setSystemProperties(properties)
    }

    /**
     * {@inheritDoc}
     */
    override fun systemProperties(properties: MutableMap<String?, *>): Test {
        forkOptions.systemProperties(properties)
        return this
    }

    /**
     * {@inheritDoc}
     */
    override fun systemProperty(name: String, value: Any): Test {
        forkOptions.systemProperty(name, value)
        return this
    }

    /**
     * {@inheritDoc}
     */
    @ToBeReplacedByLazyProperty
    override fun getBootstrapClasspath(): FileCollection {
        return forkOptions.getBootstrapClasspath()
    }

    /**
     * {@inheritDoc}
     */
    override fun setBootstrapClasspath(classpath: FileCollection) {
        forkOptions.setBootstrapClasspath(classpath)
    }

    /**
     * {@inheritDoc}
     */
    override fun bootstrapClasspath(vararg classpath: Any?): Test {
        forkOptions.bootstrapClasspath(*classpath)
        return this
    }

    /**
     * {@inheritDoc}
     */
    @ToBeReplacedByLazyProperty
    override fun getMinHeapSize(): String {
        return forkOptions.getMinHeapSize()!!
    }

    /**
     * {@inheritDoc}
     */
    @ToBeReplacedByLazyProperty
    override fun getDefaultCharacterEncoding(): String {
        return forkOptions.getDefaultCharacterEncoding()!!
    }

    /**
     * {@inheritDoc}
     */
    override fun setDefaultCharacterEncoding(defaultCharacterEncoding: String) {
        forkOptions.setDefaultCharacterEncoding(defaultCharacterEncoding)
    }

    /**
     * {@inheritDoc}
     */
    override fun setMinHeapSize(heapSize: String) {
        forkOptions.setMinHeapSize(heapSize)
    }

    /**
     * {@inheritDoc}
     */
    @ToBeReplacedByLazyProperty
    override fun getMaxHeapSize(): String {
        return forkOptions.getMaxHeapSize()!!
    }

    /**
     * {@inheritDoc}
     */
    override fun setMaxHeapSize(heapSize: String) {
        forkOptions.setMaxHeapSize(heapSize)
    }

    /**
     * {@inheritDoc}
     */
    @ToBeReplacedByLazyProperty
    override fun getJvmArgs(): MutableList<String?> {
        return forkOptions.getJvmArgs()!!
    }

    /**
     * {@inheritDoc}
     */
    @ToBeReplacedByLazyProperty
    override fun getJvmArgumentProviders(): MutableList<CommandLineArgumentProvider?> {
        return forkOptions.getJvmArgumentProviders()
    }

    /**
     * {@inheritDoc}
     */
    override fun setJvmArgs(arguments: MutableList<String?>) {
        forkOptions.setJvmArgs(arguments)
    }

    /**
     * {@inheritDoc}
     */
    override fun setJvmArgs(arguments: Iterable<*>) {
        forkOptions.setJvmArgs(arguments)
    }

    /**
     * {@inheritDoc}
     */
    override fun jvmArgs(arguments: Iterable<*>): Test {
        forkOptions.jvmArgs(arguments)
        return this
    }

    /**
     * {@inheritDoc}
     */
    override fun jvmArgs(vararg arguments: Any?): Test {
        forkOptions.jvmArgs(*arguments)
        return this
    }

    /**
     * {@inheritDoc}
     */
    @ToBeReplacedByLazyProperty
    override fun getEnableAssertions(): Boolean {
        return forkOptions.getEnableAssertions()
    }

    /**
     * {@inheritDoc}
     */
    override fun setEnableAssertions(enabled: Boolean) {
        forkOptions.setEnableAssertions(enabled)
    }

    /**
     * {@inheritDoc}
     */
    @ToBeReplacedByLazyProperty
    override fun getDebug(): Boolean {
        return forkOptions.getDebug()
    }

    /**
     * {@inheritDoc}
     */
    @Option(
        option = "debug-jvm",
        description = "Enable debugging for the test process. The process is started suspended and listening on port 5005."
    )
    override fun setDebug(enabled: Boolean) {
        forkOptions.setDebug(enabled)
    }


    /**
     * {@inheritDoc}
     */
    override fun getDebugOptions(): JavaDebugOptions {
        return forkOptions.getDebugOptions()
    }

    /**
     * {@inheritDoc}
     */
    override fun debugOptions(action: Action<JavaDebugOptions?>) {
        forkOptions.debugOptions(action)
    }

    /**
     * Enables fail fast behavior causing the task to fail on the first failed test.
     */
    @Option(option = "fail-fast", description = "Stops test execution after the first failed test.")
    public override fun setFailFast(failFast: Boolean) {
        super.setFailFast(failFast)
    }

    /**
     * Indicates if this task will fail on the first failed test
     *
     * @return whether this task will fail on the first failed test
     */
    @ToBeReplacedByLazyProperty
    public override fun getFailFast(): Boolean {
        return super.getFailFast()
    }

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
    @Incubating
    @Input
    @Option(option = "test-dry-run", description = "Simulate test execution.")
    public abstract fun getDryRun(): Property<Boolean?>?

    /**
     * {@inheritDoc}
     */
    @ToBeReplacedByLazyProperty
    override fun getAllJvmArgs(): MutableList<String?> {
        return forkOptions.getAllJvmArgs()
    }

    /**
     * {@inheritDoc}
     */
    override fun setAllJvmArgs(arguments: MutableList<String?>) {
        forkOptions.setAllJvmArgs(arguments)
    }

    /**
     * {@inheritDoc}
     */
    override fun setAllJvmArgs(arguments: Iterable<*>) {
        forkOptions.setAllJvmArgs(arguments)
    }

    /**
     * {@inheritDoc}
     */
    @Internal
    @ToBeReplacedByLazyProperty
    override fun getEnvironment(): MutableMap<String?, Any?> {
        return forkOptions.getEnvironment()
    }

    /**
     * {@inheritDoc}
     */
    override fun environment(environmentVariables: MutableMap<String?, *>): Test {
        forkOptions.environment(environmentVariables)
        return this
    }

    /**
     * {@inheritDoc}
     */
    override fun environment(name: String, value: Any): Test {
        forkOptions.environment(name, value)
        return this
    }

    /**
     * {@inheritDoc}
     */
    override fun setEnvironment(environmentVariables: MutableMap<String?, *>) {
        forkOptions.setEnvironment(environmentVariables)
    }

    /**
     * {@inheritDoc}
     */
    override fun copyTo(target: ProcessForkOptions): Test {
        forkOptions.copyTo(target)
        copyToolchainAsExecutable(target)
        return this
    }

    /**
     * {@inheritDoc}
     */
    override fun copyTo(target: JavaForkOptions): Test {
        forkOptions.copyTo(target)
        copyToolchainAsExecutable(target)
        return this
    }

    private fun copyToolchainAsExecutable(target: ProcessForkOptions) {
        val executable: String? = getJavaLauncher().get().getExecutablePath().toString()
        target.setExecutable(executable)
    }

    /**
     * Returns the module path handling of this test task.
     *
     * @since 6.4
     */
    @Nested
    public fun getModularity(): ModularitySpec {
        return field
    }

    /**
     * {@inheritDoc}
     *
     * @since 4.4
     */
    protected override fun createTestExecutionSpec(): JvmTestExecutionSpec {
        validateExecutableMatchesToolchain()
        val javaForkOptions: JavaForkOptions = getForkOptionsFactory()!!.newJavaForkOptions()
        copyTo(javaForkOptions)
        javaForkOptions.systemProperty(TestWorker.WORKER_TMPDIR_SYS_PROPERTY, File(getTemporaryDir(), "work"))
        val javaModuleDetector = getJavaModuleDetector()
        val testIsModule = javaModuleDetector.isModule(modularity.getInferModulePath().get(), getTestClassesDirs())
        val classpath = javaModuleDetector.inferClasspath(testIsModule, stableClasspath)
        val modulePath = javaModuleDetector.inferModulePath(testIsModule, stableClasspath)
        return JvmTestExecutionSpec(
            getTestFramework(),
            classpath,
            modulePath,
            getCandidateClassFiles(),
            isScanForTestClasses(),
            getTestClassesDirs(),
            getPath(),
            getIdentityPath(),
            getForkEvery(),
            javaForkOptions,
            getMaxParallelForks(),
            getPreviousFailedTestClasses(),
            testIsModule
        )
    }

    private fun validateExecutableMatchesToolchain() {
        val toolchainExecutable = getJavaLauncher().get().getExecutablePath().getAsFile()
        val customExecutable = getExecutable()
        JavaExecutableUtils.validateExecutable(
            customExecutable, "Toolchain from `executable` property",
            toolchainExecutable, "toolchain from `javaLauncher` property"
        )
    }

    private fun getPreviousFailedTestClasses(): MutableSet<String?> {
        val serializer = TestResultSerializer(getBinaryResultsDirectory().getAsFile().get())
        if (serializer.isHasResults()) {
            val previousFailedTestClasses: MutableSet<String?> = HashSet<String?>()
            serializer.read(object : Action<TestClassResult?> {
                override fun execute(testClassResult: TestClassResult) {
                    if (testClassResult.getFailuresCount() > 0) {
                        previousFailedTestClasses.add(testClassResult.getClassName())
                    }
                }
            })
            return previousFailedTestClasses
        } else {
            return mutableSetOf<String?>()
        }
    }

    @TaskAction
    public override fun executeTests() {
        val javaVersion = getJavaVersion()
        if (!javaVersion.isJava6Compatible()) {
            throw UnsupportedJavaRuntimeException("Support for test execution using Java 5 or earlier was removed in Gradle 3.0.")
        }
        if (!javaVersion.isJava8Compatible()) {
            if (testFramework.get() is JUnitPlatformTestFramework) {
                throw UnsupportedJavaRuntimeException("Running tests with JUnit platform requires a Java 8+ toolchain.")
            } else {
                DeprecationLogger.deprecate("Running tests on Java versions earlier than 8")
                    .willBecomeAnErrorInGradle9()
                    .withUpgradeGuideSection(8, "minimum_test_jvm_version")
                    .nagUser()
            }
        }

        if (getDebug()) {
            getLogger().info("Running tests for remote debugging.")
        }

        try {
            super.executeTests()
        } finally {
            CompositeStoppable.stoppable(getTestFramework()).stop()
        }
    }

    protected override fun createTestExecuter(): TestExecuter<JvmTestExecutionSpec?> {
        if (testExecuter == null) {
            return DefaultTestExecuter(
                getProcessBuilderFactory(), getActorFactory(), getModuleRegistry(),
                getServices().get(WorkerLeaseService::class.java),
                getServices().get(StartParameter::class.java).getMaxWorkerCount(),
                getServices().get(Clock::class.java),
                getServices().get(DocumentationRegistry::class.java),
                getFilter() as DefaultTestFilter?
            )
        } else {
            return testExecuter!!
        }
    }

    protected override fun getNoMatchingTestErrorReasons(): MutableList<String?> {
        val reasons: MutableList<String?> = ArrayList<String?>()
        if (!getIncludes().isEmpty()) {
            reasons.add(getIncludes().toString() + "(include rules)")
        }
        if (!getExcludes().isEmpty()) {
            reasons.add(getExcludes().toString() + "(exclude rules)")
        }
        reasons.addAll(super.getNoMatchingTestErrorReasons())
        return reasons
    }

    /**
     * Adds include patterns for the files in the test classes directory (e.g. '**&#47;*Test.class')).
     *
     * @see .setIncludes
     */
    override fun include(vararg includes: String?): Test {
        patternSet!!.include(*includes)
        return this
    }

    /**
     * Adds include patterns for the files in the test classes directory (e.g. '**&#47;*Test.class')).
     *
     * @see .setIncludes
     */
    override fun include(includes: Iterable<String?>): Test {
        patternSet!!.include(includes)
        return this
    }

    /**
     * {@inheritDoc}
     */
    override fun include(includeSpec: Spec<FileTreeElement?>): Test {
        patternSet!!.include(includeSpec)
        return this
    }

    /**
     * {@inheritDoc}
     */
    override fun include(includeSpec: Closure<*>): Test {
        patternSet!!.include(includeSpec)
        return this
    }

    /**
     * Adds exclude patterns for the files in the test classes directory (e.g. '**&#47;*Test.class')).
     *
     * @see .setExcludes
     */
    override fun exclude(vararg excludes: String?): Test {
        patternSet!!.exclude(*excludes)
        return this
    }

    /**
     * Adds exclude patterns for the files in the test classes directory (e.g. '**&#47;*Test.class')).
     *
     * @see .setExcludes
     */
    override fun exclude(excludes: Iterable<String?>): Test {
        patternSet!!.exclude(excludes)
        return this
    }

    /**
     * {@inheritDoc}
     */
    override fun exclude(excludeSpec: Spec<FileTreeElement?>): Test {
        patternSet!!.exclude(excludeSpec)
        return this
    }

    /**
     * {@inheritDoc}
     */
    override fun exclude(excludeSpec: Closure<*>): Test {
        patternSet!!.exclude(excludeSpec)
        return this
    }

    /**
     * {@inheritDoc}
     */
    public override fun setTestNameIncludePatterns(testNamePattern: MutableList<String?>): Test {
        super.setTestNameIncludePatterns(testNamePattern)
        return this
    }

    /**
     * Returns the directories for the compiled test sources.
     *
     * @return All test class directories to be used.
     * @since 4.0
     */
    @Internal
    @ToBeReplacedByLazyProperty
    public fun getTestClassesDirs(): FileCollection {
        return testClassesDirs!!
    }

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
    public fun setTestClassesDirs(testClassesDirs: FileCollection) {
        this.testClassesDirs = testClassesDirs
    }

    /**
     * Returns the include patterns for test execution.
     *
     * @see .include
     */
    @Internal
    @ToBeReplacedByLazyProperty
    override fun getIncludes(): MutableSet<String?> {
        return patternSet!!.getIncludes()
    }

    /**
     * Sets the include patterns for test execution.
     *
     * @param includes The patterns list
     * @see .include
     */
    override fun setIncludes(includes: Iterable<String?>): Test {
        patternSet!!.setIncludes(includes)
        return this
    }

    /**
     * Returns the exclude patterns for test execution.
     *
     * @see .exclude
     */
    @Internal
    @ToBeReplacedByLazyProperty
    override fun getExcludes(): MutableSet<String?> {
        return patternSet!!.getExcludes()
    }

    /**
     * Sets the exclude patterns for test execution.
     *
     * @param excludes The patterns list
     * @see .exclude
     */
    override fun setExcludes(excludes: Iterable<String?>): Test {
        patternSet!!.setExcludes(excludes)
        return this
    }

    /**
     * Returns the configured [TestFramework].
     *
     * @since 7.3
     */
    @Nested
    public fun getTestFrameworkProperty(): Property<TestFramework> {
        return testFramework
    }

    @Internal
    @ToBeReplacedByLazyProperty(comment = "This will be removed")
    public fun getTestFramework(): TestFramework {
        // TODO: Deprecate and remove this method
        return testFramework.get()
    }

    public fun testFramework(testFrameworkConfigure: Closure<*>?): TestFramework {
        // TODO: Deprecate and remove this method
        options(testFrameworkConfigure!!)
        return getTestFramework()
    }

    /**
     * Configures test framework specific options.
     *
     *
     * When a `Test` task is created outside of Test Suites, you should call [.useJUnit], [.useJUnitPlatform] or [.useTestNG] before using this method.
     * If no test framework has been set, the task will assume JUnit4.
     *
     * @return The test framework options.
     * @since 3.5
     */
    public fun options: TestFrameworkOptions?

    /**
     * Specifies that JUnit4 should be used to discover and execute the tests.
     *
     * @see .useJUnit
     */
    public fun useJUnit()

    /**
     * Specifies that JUnit4 should be used to discover and execute the tests with additional configuration.
     *
     *
     * The supplied action configures an instance of [JUnit4 specific options][JUnitOptions].
     *
     * @param testFrameworkConfigure An action used to configure JUnit4 options.
     * @since 3.5
     */
    public fun useJUnit:JUnitOptions?

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
    public fun useJUnitPlatform()

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
    public fun useJUnitPlatform:JUnitPlatformOptions?

    /**
     * Specifies that TestNG should be used to discover and execute the tests.
     *
     * @see .useTestNG
     */
    public fun useTestNG()

    /**
     * Specifies that TestNG should be used to discover and execute the tests with additional configuration.
     *
     *
     * The supplied action configures an instance of [TestNG specific options][TestNGOptions].
     *
     * @param testFrameworkConfigure An action used to configure TestNG options.
     * @since 3.5
     */
    public fun useTestNG:TestNGOptions

    /**
     * Set the framework, only if it is being changed to a new value.
     *
     * If we are setting a framework to its existing value, no-op so as not to overwrite existing options here.
     * We need to allow this especially for the default test task, so that existing builds that configure options and
     * then call useJunit() don't clear out their options.
     */
    public fun useTestFramework(testFramework: TestFramework)

    public fun setScanForTestClasses(scanForTestClasses: Boolean)

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
    public fun setForkEvery(forkEvery: Long)

    /**
     * Sets the maximum number of test processes to start in parallel.
     *
     *
     * By default, Gradle executes a single test class at a time but allows multiple [Test] tasks to run in parallel.
     *
     *
     * @param maxParallelForks The maximum number of forked test processes. Use 1 to disable parallel test execution for this task.
     */
    public fun setMaxParallelForks(maxParallelForks: Int)

    /**
     * Executes the action against the [.getFilter].
     *
     * @param action configuration of the test filter
     * @since 1.10
     */
    public fun filter:TestFilterImpl

    /**
     * Sets the testExecuter property.
     *
     * @since 4.2
     */
    public fun setTestExecuter(testExecuter: TestExecuter<JvmTestExecutionSpec?>) {
        this.testExecuter = testExecuter
    }

    /**
     * Configures the java executable to be used to run the tests.
     *
     * @since 6.7
     */
    public fun getJavaLauncher(): Property<JavaLauncher?>

    public override fun testsAreNotFiltered(): Boolean

}
