@file:JvmName("SerialDescriptorJvmCommon")
package klib.data.type.serialization

import klib.data.type.reflection.callMember
import kotlin.reflect.KClass
import kotlinx.serialization.descriptors.SerialDescriptor

@Suppress("UNCHECKED_CAST")
public val SerialDescriptor.typeParameterDescriptors: Array<SerialDescriptor>
    get() = callMember("typeParameterDescriptors") as Array<SerialDescriptor>

public val SerialDescriptor.kClass: KClass<*>
    get() = Class.forName(serialName).kotlin
