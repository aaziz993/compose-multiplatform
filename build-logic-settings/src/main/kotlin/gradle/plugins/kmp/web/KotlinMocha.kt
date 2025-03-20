package gradle.plugins.kmp.web

import gradle.api.trySet
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.targets.js.testing.mocha.KotlinMocha

@Serializable
internal data class KotlinMocha(
    val timeout: String? = null
) {

    fun applyTo(recipient: KotlinMocha) {
        mocha::timeout trySet mocha.timeout
    }
}
