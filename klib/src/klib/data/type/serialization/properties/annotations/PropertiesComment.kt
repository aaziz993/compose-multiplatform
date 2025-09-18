package klib.data.type.serialization.properties.annotations

import kotlinx.serialization.SerialInfo

/**
 * Adds a comment block before property on serialization
 * @property lines comment lines to add
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.BINARY)
@SerialInfo
public annotation class PropertiesComment(
    vararg val lines: String,
)