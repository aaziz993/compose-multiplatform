package klib.data.entity.annotation

import kotlinx.serialization.SerialInfo


@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class Property(val name: String)
