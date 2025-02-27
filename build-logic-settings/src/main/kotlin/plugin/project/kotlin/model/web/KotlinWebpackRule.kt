package plugin.project.kotlin.model.web

import gradle.tryAssign
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackRule

internal interface KotlinWebpackRule {

    val enabled: Boolean?

    /**
     * Raw rule `test` field value. Needs to be wrapped in quotes when using string notation.
     */
    val test: String?
    val include: List<String>?
    val exclude: List<String>?

    /**
     * Validates the rule state just before it getting applied.
     * Returning false will skip the rule silently. To terminate the build instead, throw an error.
     */
    val validate: Boolean?

    fun applyTo(webpackRule: KotlinWebpackRule) {
        webpackRule.enabled tryAssign enabled
        webpackRule.test tryAssign test
        webpackRule.include tryAssign include
        webpackRule.exclude tryAssign exclude
        validate?.takeIf { it }?.run { webpackRule.validate() }
    }
}
