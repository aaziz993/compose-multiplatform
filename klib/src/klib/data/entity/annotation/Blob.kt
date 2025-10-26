package klib.data.entity.annotation

import kotlinx.serialization.SerialInfo

@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class Blob(val useObjectIdentifier: Boolean = false)