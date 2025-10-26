package klib.data.entity.annotation

import kotlinx.serialization.SerialInfo

@SerialInfo
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
public annotation class ForeignKey(
    val name: String = "",
    val onUpdate: String = "",
    val onDelete: String = ""
)
