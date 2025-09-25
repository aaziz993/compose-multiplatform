package klib.data.type.serialization.properties.annotations

import kotlinx.serialization.SerialInfo

/**
 * Specifies whether encounters of unknown properties (i.e., properties not declared in the class) in the input JSON
 * should be ignored instead of throwing [SerializationException].
 *
 * With this annotation, it is possible to allow unknown properties for annotated classes, while
 * general decoding methods (such as [klib.data.type.serialization.properties.Properties.decodeFromString] and others) would still reject them for everything else.
 * If you want [klib.data.type.serialization.properties.Properties.decodeFromString] allow all unknown properties for all classes and inputs, consider using
 * [PropertiesBuilder.ignoreUnknownKeys].
 *
 * Example:
 * ```
 * @Serializable
 * @PropertiesIgnoreUnknownKeys
 * class Outer(val a: Int, val inner: Inner)
 *
 * @Serializable
 * class Inner(val x: String)
 *
 * // Throws SerializationException because there is no "unknownKey" property in Inner
 * Properties.decodeFromString<Outer>("""{"a":1,"inner":{"x":"value","unknownKey":"unknownValue"}}""")
 *
 * // Decodes successfully despite "unknownKey" property in Outer
 * Properties.decodeFromString<Outer>("""{"a":1,"inner":{"x":"value"}, "unknownKey":42}""")
 * ```
 *
 * @see PropertiesBuilder.ignoreUnknownKeys
 */
@SerialInfo
@Target(AnnotationTarget.CLASS)
public annotation class PropertiesIgnoreUnknownKeys