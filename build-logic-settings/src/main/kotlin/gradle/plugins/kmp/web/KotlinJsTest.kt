package gradle.plugins.kmp.web

import gradle.accessors.moduleName
import gradle.api.tasks.applyTo
import gradle.api.tasks.test.TestFilter
import gradle.api.tasks.test.TestLoggingContainer
import gradle.api.tryAssign
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.tasks.KotlinTest
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest

@Serializable
internal data class KotlinJsTest(
    override val targetName: String? = null,
    override val ignoreRunFailures: Boolean? = null,
    override val binaryResultsDirectory: String? = null,
    override val ignoreFailures: Boolean? = null,
    override val testLogging: TestLoggingContainer? = null,
    override val testNameIncludePatterns: List<String>? = null,
    override val failFast: Boolean? = null,
    override val filter: TestFilter? = null,
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
    override val name: String? = null,
    val environment: Map<String, String>? = null,
    val inputFileProperty: String? = null,
    val debug: Boolean? = null,
    val nodeJsArgs: List<String>? = null,
    val useMocha: KotlinMocha? = null,
    val useKarma: KotlinKarma? = null,
) : KotlinTest<KotlinJsTest>() {

    context(project: Project)
    override fun applyTo(receiver: KotlinJsTest) {
        super.applyTo(receiver)

        environment?.let(receiver.environment::putAll)
        receiver.inputFileProperty tryAssign inputFileProperty?.let(project::file)
        receiver::debug trySet debug
        nodeJsArgs?.let(receiver.nodeJsArgs::addAll)
        useMocha?.applyTo(receiver.useMocha())
        useKarma?.applyTo(receiver.useKarma(), "${project.moduleName}-targetName")
    }

    context(project: Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<KotlinJsTest>())
}
