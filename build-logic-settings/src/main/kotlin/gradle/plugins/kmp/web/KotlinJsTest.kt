package gradle.plugins.kmp.web

import gradle.accessors.moduleName

import gradle.api.tasks.test.DefaultTestFilter
import gradle.api.tasks.test.TestLoggingContainer
import gradle.api.tryAssign
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.plugins.buildconfig.generator.BuildConfigKotlinGenerator
import gradle.plugins.kotlin.tasks.KotlinTest
import kotlinx.serialization.Serializable
import org.gradle.api.Project
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
    override val name: String = "",
    val environment: Map<String, String>? = null,
    val inputFileProperty: String? = null,
    val debug: Boolean? = null,
    val nodeJsArgs: List<String>? = null,
    val useMocha: @Serializable(with = KotlinMochaSerializer::class) Any? = null,
    val useKarma: @Serializable(with = KotlinKarmaSerializer::class) Any? = null,
) : KotlinTest<KotlinJsTest>() {

    context(Project)
    override fun applyTo(recipient: KotlinJsTest) {
        super.applyTo(recipient)

        environment?.let(recipient.environment::putAll)
        recipient.inputFileProperty tryAssign inputFileProperty?.let(::file)
        recipient::debug trySet debug
        nodeJsArgs?.let(recipient.nodeJsArgs::addAll)

        when (useMocha) {
            is Boolean -> recipient.useKarma()
            is KotlinMocha -> useMocha.applyTo(recipient.useMocha())
            else -> Unit
        }

        when (useKarma) {
            is Boolean -> recipient.useKarma()
            is KotlinKarma -> useKarma.applyTo(recipient.useKarma(), "$moduleName-targetName")
            else -> Unit
        }
    }
}
