package klib.data.entity.annotation

import kotlinx.serialization.SerialInfo

@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class Enum(val length: Int)