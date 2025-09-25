package klib.data.type.serialization.yaml

import com.charleskorn.kaml.YamlInput
import klib.data.type.serialization.coders.any.AnyDecoder
import kotlinx.serialization.DeserializationStrategy

internal class YamlAnyDecoder(decoder: YamlInput) : AnyDecoder<YamlInput>(decoder) {

    override fun decodeValue(): Any? = decoder.yaml.decodeAnyFromYamlNode(decoder.node)

    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>, value: Any?): T =
        decoder.yaml.decodeFromAny(deserializer, value)
}
