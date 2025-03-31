package gradle.plugins.kotlin.targets.web

import gradle.accessors.moduleName
import gradle.api.file.tryAssign
import gradle.api.tasks.applyTo
import gradle.api.tasks.test.TestFilter
import gradle.api.tasks.test.TestLoggingContainer
import gradle.collection.SerializableAnyMap
import gradle.collection.tryAddAll
import gradle.collection.tryPutAll
import gradle.collection.trySet
import gradle.plugins.kotlin.tasks.KotlinTest
import gradle.reflect.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest

@Serializable
internal data class KotlinJsTest(
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
    override val targetName: String? = null,
    val environment: Map<String, String>? = null,
    val setEnvironment: Map<String, String>? = null,
    val inputFileProperty: String? = null,
    val debug: Boolean? = null,
    val nodeJsArgs: List<String>? = null,
    val useMocha: KotlinMocha? = null,
    val useKarma: KotlinKarma? = null,
) : KotlinTest<KotlinJsTest>() {

    context(Project)
    override fun applyTo(receiver: KotlinJsTest) {
        super.applyTo(receiver)

        receiver.environment tryPutAll environment
        receiver.environment trySet setEnvironment
        receiver.inputFileProperty tryAssign inputFileProperty?.let(project::file)
        receiver::debug trySet debug
        receiver.nodeJsArgs tryAddAll nodeJsArgs

        useMocha?.let { useMocha ->
            receiver.useMocha(useMocha::applyTo)
        }

        useKarma?.applyTo(receiver.useKarma(), "${project.moduleName}-targetName")
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<KotlinJsTest>())
}
