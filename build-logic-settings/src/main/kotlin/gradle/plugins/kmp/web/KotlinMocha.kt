package gradle.plugins.kmp.web

import gradle.api.trySet
import kotlinx.serialization.Serializable

@Serializable
internal data class KotlinMocha(
    val timeout: String? = null
) {

    fun applyTo(receiver: org.jetbrains.kotlin.gradle.targets.js.testing.mocha.KotlinMocha) {
        receiver::timeout trySet timeout
    }
}
