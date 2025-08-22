@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.yaml

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlOutput
import klib.data.type.serialization.coders.any.AnyEncoder
import kotlinx.serialization.SerializationStrategy

internal class YamlAnyEncoder(encoder: YamlOutput) : AnyEncoder<YamlOutput>(encoder) {

    private val yaml = Yaml.default

    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T): Any? =
        yaml.encodeToAny(serializer, value)

    override fun encodeValue(value: Any?): Unit =
        encoder.encodeYamlNode(yaml.encodeAnyToYamlNode(value))
}
