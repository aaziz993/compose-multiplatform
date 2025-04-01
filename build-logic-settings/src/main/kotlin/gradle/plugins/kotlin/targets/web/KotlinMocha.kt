package gradle.plugins.kotlin.targets.web

import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable

@Serializable
internal data class KotlinMocha(
    val timeout: String? = null
) {

    fun applyTo(receiver: org.jetbrains.kotlin.gradle.targets.js.testing.mocha.KotlinMocha) {
        receiver::timeout trySet timeout
    }
}
