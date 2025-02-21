package gradle.serialization.serializer

import gradle.serialization.decodeAnyFromString
import gradle.serialization.encodeAnyToString
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

internal object AnySerializer : KSerializer<Any> {

    override val descriptor: SerialDescriptor
        get() = String::class.serializer().descriptor

    override fun serialize(encoder: Encoder, value: Any) {
        Json.Default.encodeAnyToString(value)
    }

    override fun deserialize(decoder: Decoder): Any = Json.Default.decodeAnyFromString(decoder.decodeString())!!
}
