@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.coders.any

import app.softwork.serialization.csv.CSVEncoder
import com.charleskorn.kaml.YamlOutput
import klib.data.type.serialization.coders.tree.TreeAnyEncoder
import klib.data.type.serialization.coders.tree.TreeEncoder
import klib.data.type.serialization.csv.CSVAnyEncoder
import klib.data.type.serialization.json.JsonAnyEncoder
import klib.data.type.serialization.properties.PropertiesAnyEncoder
import klib.data.type.serialization.properties.PropertiesEncoder
import klib.data.type.serialization.toml.TomlAnyEncoder
import klib.data.type.serialization.xml.XmlAnyEncoder
import klib.data.type.serialization.yaml.YamlAnyEncoder
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonEncoder
import net.peanuuutz.tomlkt.TomlEncoder
import nl.adaptivity.xmlutil.serialization.XmlEncoderBase.XmlEncoder

public abstract class AnyEncoder<T : Encoder>(public val encoder: T) {

    public abstract fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T): Any?

    public abstract fun encodeValue(value: Any?)
}

public val ANY_ENCODERS: MutableList<Encoder.() -> AnyEncoder<out Encoder>?> = mutableListOf(
    { (this as? TreeEncoder)?.let(::TreeAnyEncoder) },
    { (this as? JsonEncoder)?.let(::JsonAnyEncoder) },
    { (this as? YamlOutput)?.let(::YamlAnyEncoder) },
    { (this as? TomlEncoder)?.let(::TomlAnyEncoder) },
    { (this as? PropertiesEncoder)?.let(::PropertiesAnyEncoder) },
    { (this as? CSVEncoder)?.let(::CSVAnyEncoder) },
    { (this as? XmlEncoder)?.let(::XmlAnyEncoder) },
)

public val Encoder.asAnyEncoder: AnyEncoder<out Encoder>
    get() = ANY_ENCODERS.firstNotNullOfOrNull { fn -> fn(this) }
        ?: throw SerializationException("Unsupported decoder: ${this::class.simpleName}")
