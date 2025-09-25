@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.xml

import klib.data.type.collections.map.asMap
import klib.data.type.serialization.coders.any.AnyEncoder
import kotlinx.serialization.SerializationStrategy
import nl.adaptivity.xmlutil.XmlEvent
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlEncoderBase.XmlEncoder

internal class XmlAnyEncoder(encoder: XmlEncoder) : AnyEncoder<XmlEncoder>(encoder) {

    private val xml = XML(encoder.config)

    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T): Any =
        xml.encodeToMap(serializer, value)

    override fun encodeValue(value: Any?): Unit =
        value!!.asMap<XmlEvent.StartElementEvent, Any>().encodeMapToWriter(encoder.target)
}
