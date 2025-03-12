package gradle.plugins.java

import gradle.serialization.serializer.AnySerializer
import gradle.tasks.test.DefaultTestFilter
import gradle.tasks.test.TestLoggingContainer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest

@Serializable
internal data class KotlinJvmTest(
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
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    override val name: String = "",
    override val systemProperties: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
    override val defaultCharacterEncoding: String? = null,
    override val minHeapSize: String? = null,
    override val maxHeapSize: String? = null,
    override val jvmArgs: List<String>? = null,
    override val bootstrapClasspath: List<String>? = null,
    override val enableAssertions: Boolean? = null,
    override val debug: Boolean? = null,
    override val debugOptions: JavaDebugOptions? = null,
    override val allJvmArgs: List<String>? = null,
    override val executable: String? = null,
    override val workingDir: String? = null,
    override val environment: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
    override val includes: List<String>? = null,
    override val setIncludes: List<String>? = null,
    override val excludes: List<String>? = null,
    override val setExcludes: List<String>? = null,
    val targetName: String? = null,
) : Test() {

    context(Project)
    override fun applyTo() =
        super.applyTo(tasks.withType<KotlinJvmTest>())
}
