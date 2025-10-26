package klib.data.entity.annotation

import kotlinx.serialization.SerialInfo

@SerialInfo
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
public annotation class Index(
    val indexName: String = "",
    val indexType: String = "",
    val isUnique: Boolean = false,
    val columns: Array<String> = [],
)
