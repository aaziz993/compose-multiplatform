@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.xml

import klib.data.type.cast
import klib.data.type.serialization.coders.any.AnyDecoder
import kotlinx.serialization.DeserializationStrategy
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlDecoderBase.XmlDecoder

internal class XmlAnyDecoder(decoder: XmlDecoder) : AnyDecoder<XmlDecoder>(decoder) {

    private val xml = XML(decoder.config)

    override fun decodeValue(): Any = decoder.input.decodeMapFromReader()

    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>, value: Any?): T =
        xml.decodeFromMap(deserializer, value!!.cast())
}
