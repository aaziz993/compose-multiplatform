package gradle.plugins.kotlin.targets.web

import gradle.api.provider.tryAssign
import gradle.reflect.trySet
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

    fun applyTo(receiver: KotlinWebpackRule) {
        receiver.enabled tryAssign enabled
        receiver.test tryAssign test
        receiver.include tryAssign include
        receiver.exclude tryAssign exclude
        receiver::validate trySet validate
    }
}
