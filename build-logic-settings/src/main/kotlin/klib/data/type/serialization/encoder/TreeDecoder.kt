package klib.data.type.serialization.encoder

import com.charleskorn.kaml.YamlInput
import klib.data.type.serialization.decodeAnyFromJsonElement
import klib.data.type.serialization.decodeAnyFromTomlElement
import klib.data.type.serialization.yaml.decodeAnyFromYamlNode
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.JsonDecoder
import net.peanuuutz.tomlkt.TomlDecoder

public sealed class TreeDecoder : Decoder {
    public abstract fun decodeAny(): Any?
}

private class JsonTreeDecoder(
    private val decoder: JsonDecoder,
) : TreeDecoder(), Decoder by decoder {
    override fun decodeAny(): Any? =
        decoder.json.decodeAnyFromJsonElement(decoder.decodeJsonElement())
}

private class YamlTreeDecoder(
    private val decoder: YamlInput,
) : TreeDecoder(), Decoder by decoder {
    override fun decodeAny(): Any? = decoder.yaml.decodeAnyFromYamlNode(decoder.node)
}

private class TomlTreeDecoder(
    private val decoder: TomlDecoder,
) : TreeDecoder(), Decoder by decoder {
    override fun decodeAny(): Any? = decoder.toml.decodeAnyFromTomlElement(decoder.decodeTomlElement())
}

public fun Decoder.decodeAny(): Any? = when (this) {
    is AnyTreeDecoder -> this
    is JsonDecoder -> JsonTreeDecoder(this)
    is YamlInput -> YamlTreeDecoder(this)
    is TomlDecoder -> TomlTreeDecoder(this)
    else -> error("Unsupported decoder type: ${this::class.simpleName}")
}.decodeAny()
