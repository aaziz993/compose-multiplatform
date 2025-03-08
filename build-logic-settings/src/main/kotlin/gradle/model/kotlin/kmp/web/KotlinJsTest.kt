package gradle.model.kotlin.kmp.web

import gradle.model.DefaultTestFilter
import gradle.model.TestLoggingContainer
import gradle.model.kotlin.KotlinTest
import gradle.serialization.serializer.AnySerializer
import gradle.tryAssign
import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Named
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
    val environment: Map<String, String>? = null,
    val inputFileProperty: String? = null,
    val debug: Boolean? = null,
    val nodeJsArgs: List<String>? = null,
    val useMocha: Boolean? = null,
    val useMochaDsl: KotlinMocha? = null,
    val useKarma: Boolean? = null,
    val useKarmaDsl: KotlinKarma? = null,
) : KotlinTest {

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as KotlinJsTest

        environment?.let(named.environment::putAll)
        named.inputFileProperty tryAssign inputFileProperty?.let(::file)
        named::debug trySet debug
        nodeJsArgs?.let(named.nodeJsArgs::addAll)

        useMocha?.takeIf { it }?.run { named.useMocha() }

        useMochaDsl?.let { useMochaDsl ->
            named.useMocha(useMochaDsl::applyTo)
        }

        useKarma?.takeIf { it }?.run { named.useKarma() }

        useKarmaDsl?.let { useKarmaDsl ->
            named.useKarma {
                useKarmaDsl.applyTo(this)
            }
        }
    }
}
