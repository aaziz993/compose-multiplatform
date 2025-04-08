package klib.data.type.serialization.encoder

import com.charleskorn.kaml.YamlInput
import klib.data.type.serialization.decodeAnyFromJsonElement
import klib.data.type.serialization.decodeAnyFromTomlElement
import klib.data.type.serialization.yaml.decodeAnyFromYamlNode
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.JsonDecoder
import net.peanuuutz.tomlkt.TomlDecoder

public sealed class TreeDecoder : Decoder {
    abstract fun decodeAny(): Any?
}

@PublishedApi
internal class JsonTreeDecoder(
    private val decoder: JsonDecoder,
) : TreeDecoder(), Decoder by decoder {
    override fun decodeAny(): Any? =
        decoder.json.decodeAnyFromJsonElement(decoder.decodeJsonElement())
}

@PublishedApi
internal class YamlTreeDecoder(
    private val decoder: YamlInput,
) : TreeDecoder(), Decoder by decoder {
    override fun decodeAny(): Any? = decoder.yaml.decodeAnyFromYamlNode(decoder.node)
}

@PublishedApi
internal class TomlTreeDecoder(
    private val decoder: TomlDecoder,
) : TreeDecoder(), Decoder by decoder {
    override fun decodeAny(): Any? = decoder.toml.decodeAnyFromTomlElement(decoder.decodeTomlElement())
}

public val Decoder.asTreeDecoder: TreeDecoder
    get() = when (this) {
        is AnyTreeDecoder -> this
        is JsonDecoder -> JsonTreeDecoder(this)
        is YamlInput -> YamlTreeDecoder(this)
        is TomlDecoder -> TomlTreeDecoder(this)
        else -> error("Unsupported decoder type: ${this::class.simpleName}")
    }