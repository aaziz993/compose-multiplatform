package klib.data.type.serialization.json.serializer

import klib.data.type.serialization.json.decodeAnyFromJsonElement
import klib.data.type.serialization.json.encodeAnyToJsonElement
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder



public object JsonAnySerializer : JsonGenericAnySerializer<Any>() {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Any", PrimitiveKind.STRING)
}

