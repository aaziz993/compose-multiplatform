package klib.data.type.serialization.properties

import klib.data.type.serialization.coders.tree.TreeDecoder
import kotlinx.serialization.modules.SerializersModule

public class PropertiesDecoder(
    value: Any?,
    serializersModule: SerializersModule,
    configuration: PropertiesConfiguration,
) : TreeDecoder(value, serializersModule, configuration.asTreeDecoderConfiguration)
