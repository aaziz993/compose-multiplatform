@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.coders.any

import app.softwork.serialization.csv.CSVDecoder
import com.charleskorn.kaml.YamlInput
import klib.data.type.serialization.coders.tree.TreeAnyDecoder
import klib.data.type.serialization.coders.tree.TreeDecoder
import klib.data.type.serialization.csv.CSVAnyDecoder
import klib.data.type.serialization.json.JsonAnyDecoder
import klib.data.type.serialization.toml.TomlAnyDecoder
import klib.data.type.serialization.xml.XmlAnyDecoder
import klib.data.type.serialization.yaml.YamlAnyDecoder
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.JsonDecoder
import net.peanuuutz.tomlkt.TomlDecoder
import nl.adaptivity.xmlutil.serialization.XmlDecoderBase.XmlDecoder

public abstract class AnyDecoder<T : Decoder>(public val decoder: T) {

    public abstract fun decodeValue(): Any?

    public abstract fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>, value: Any?): T
}

public val ANY_DECODERS: MutableList<Decoder.() -> AnyDecoder<out Decoder>?> = mutableListOf(
    { (this as? TreeDecoder)?.let(::TreeAnyDecoder) },
    { (this as? JsonDecoder)?.let(::JsonAnyDecoder) },
    { (this as? YamlInput)?.let(::YamlAnyDecoder) },
    { (this as? TomlDecoder)?.let(::TomlAnyDecoder) },
    { (this as? CSVDecoder)?.let(::CSVAnyDecoder) },
    { (this as? XmlDecoder)?.let(::XmlAnyDecoder) },
)

public val Decoder.asAnyDecoder: AnyDecoder<out Decoder>
    get() = ANY_DECODERS.firstNotNullOfOrNull { fn -> fn(this) }
        ?: throw SerializationException("Unsupported decoder $this")
