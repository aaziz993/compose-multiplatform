package klib.data.entity.annotation

import kotlinx.serialization.SerialInfo


@SerialInfo
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
public annotation class FkReference(
    val name: String = "",
    val column: String = "",
    val targetTable: String,
    val targetColumn: String
)
