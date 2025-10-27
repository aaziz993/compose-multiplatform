package klib.data.entity.annotation

import kotlinx.serialization.SerialInfo


@SerialInfo
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
public annotation class FkReference(
    val name: String = "",
    val property: String = "",
    val targetEntity: String,
    val targetProperty: String
)
