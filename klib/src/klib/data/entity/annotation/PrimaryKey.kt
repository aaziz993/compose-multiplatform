package klib.data.entity.annotation

import kotlinx.serialization.SerialInfo

@SerialInfo
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
public annotation class PrimaryKey(
    val name: String = "",
    val columns: Array<String> = [],
)