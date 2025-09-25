package klib.data.type.serialization.annotations

import kotlinx.serialization.SerialInfo

@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class TypeSerial(val index: Int = 0)
