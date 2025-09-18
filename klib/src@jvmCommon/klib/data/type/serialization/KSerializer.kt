package klib.data.type.serialization

import kotlinx.serialization.KSerializer
import klib.data.type.reflection.callMember

@Suppress("UNCHECKED_CAST")
public val KSerializer<*>.typeParametersSerializers: Array<KSerializer<*>>
    get() = callMember("typeParametersSerializers") as Array<KSerializer<*>>
