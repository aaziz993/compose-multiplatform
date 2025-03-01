package plugin.project.kotlin.kmp.model.web

import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackCssRule

@Serializable
internal data class KotlinWebpackCssRule(
    override val enabled: Boolean? = null,
    /**
     * Raw rule `test` field value. Needs to be wrapped in quotes when using string notation.
     */
    override val test: String? = null,
    override val include: List<String>? = null,
    override val exclude: List<String>? = null,
    /**
     * Validates the rule state just before it getting applied.
     * Returning false will skip the rule silently. To terminate the build instead, throw an error.
     */
    override val validate: Boolean? = null,
    val mode: String? = null
) : KotlinWebpackRule {

    fun applyTo(
        webpackRule: KotlinWebpackCssRule
    ) {
        super.applyTo(webpackRule)
        webpackRule.mode tryAssign mode
    }
}

