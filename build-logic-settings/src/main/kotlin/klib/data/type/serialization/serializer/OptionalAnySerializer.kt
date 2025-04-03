package klib.data.type.serialization.serializer

import klib.data.type.serialization.decodeAnyFromJsonElement
import klib.data.type.serialization.decodeAnyFromString
import klib.data.type.serialization.encodeAnyToJsonElement
import klib.data.type.serialization.encodeAnyToString
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder

public object OptionalAnySerializer : GenericAnySerializer<Any?>()
