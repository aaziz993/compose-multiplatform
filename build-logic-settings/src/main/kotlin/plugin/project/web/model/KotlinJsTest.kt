package plugin.project.web.model

import gradle.tryAssign
import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest
import plugin.project.kotlin.model.language.test.DefaultTestFilter
import plugin.project.kotlin.model.language.test.KotlinTest

@Serializable
internal data class KotlinJsTest(
    override val targetName: String? = null,
    override val ignoreRunFailures: Boolean? = null,
    override val filter: DefaultTestFilter? = null,
    override val ignoreFailures: Boolean? = null,
    val environment: Map<String, String>? = null,
    val inputFileProperty: String? = null,
    val debug: Boolean? = null,
    val nodeJsArgs: List<String>? = null,
    val useMocha: Boolean? = null,
    val mocha: KotlinMocha? = null,
    val useKarma: Boolean? = null,
    val karma: KotlinKarma? = null,
) : KotlinTest {

    context(Project)
    fun applyTo(test: KotlinJsTest) {
        super.applyTo(test)
        environment?.let(test.environment::putAll)
        test.inputFileProperty tryAssign inputFileProperty?.let(::file)
        test::debug trySet debug
        nodeJsArgs?.let(test.nodeJsArgs::addAll)

        useMocha?.takeIf { it }.run { test.useMocha() }
        if (mocha != null) {
            test.useMocha(mocha::applyTo)
        }

        useKarma?.takeIf { it }.run { test.useKarma() }
        if (karma != null) {
            test.useKarma {
                karma.applyTo(this)
            }
        }
    }
}
