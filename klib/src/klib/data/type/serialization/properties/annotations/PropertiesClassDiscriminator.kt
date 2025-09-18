package klib.data.type.serialization.properties.annotations

/**
 * Specifies key for class discriminator value used during polymorphic serialization in [klib.data.type.serialization.properties.Properties].
 * Provided key is used only for an annotated class and its subclasses;
 * to configure global class discriminator, use [PropertiesBuilder.classDiscriminator]
 * property.
 *
 * This annotation is [inheritable][InheritableSerialInfo], so it should be sufficient to place it on a base class of hierarchy.
 * It is not possible to define different class discriminators for different parts of class hierarchy.
 * Pay attention to the fact that class discriminator, same as polymorphic serializer's base class, is
 * determined statically.
 *
 * Example:
 * ```
 * @Serializable
 * @PropertiesClassDiscriminator("message_type")
 * abstract class Base
 *
 * @Serializable // Class discriminator is inherited from Base
 * abstract class ErrorClass: Base()
 *
 * @Serializable
 * class Message(val message: Base, val error: ErrorClass?)
 *
 * val message = Properties.decodeFromString<Message>("""{"message": {"message_type":"my.app.BaseMessage", "message": "not found"}, "error": {"message_type":"my.app.GenericError", "error_code": 404}}""")
 * ```
 *
 * @see klib.data.type.serialization.properties.PropertiesConfiguration.classDiscriminator
 */

import kotlin.annotation.AnnotationTarget;
import kotlinx.serialization.InheritableSerialInfo;

@InheritableSerialInfo
@Target(AnnotationTarget.CLASS)
public annotation class PropertiesClassDiscriminator(val discriminator: String)