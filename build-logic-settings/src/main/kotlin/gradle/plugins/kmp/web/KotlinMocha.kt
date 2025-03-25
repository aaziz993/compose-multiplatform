package gradle.plugins.kmp.web

import gradle.api.trySet
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.internal.impldep.kotlinx.serialization.json.JsonContentPolymorphicSerializer

@Serializable
internal data class KotlinMocha(
    val timeout: String? = null
) {

    fun applyTo(receiver: org.jetbrains.kotlin.gradle.targets.js.testing.mocha.KotlinMocha) {
        receiver::timeout trySet timeout
    }
}
