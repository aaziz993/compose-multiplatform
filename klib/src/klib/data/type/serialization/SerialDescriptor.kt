package klib.data.type.serialization

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import klib.data.type.collections.containsInstance
import klib.data.type.collections.deepMinusKeys
import klib.data.type.collections.list.asList
import klib.data.type.collections.map.asMap
import klib.data.type.collections.takeIfNotEmpty
import klib.data.type.reflecion.isFloatNumber
import klib.data.type.reflecion.isIntNumber
import klib.data.type.reflecion.isUIntNumber
import klib.data.type.reflecion.kClass
import klib.data.type.serialization.serializers.bignum.BigDecimalSerializer
import klib.data.type.serialization.serializers.bignum.BigIntegerSerializer
import klib.data.type.validator.Validation
import klib.data.type.validator.Validator
import klib.data.type.validator.ValidatorRule
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.encoding.CompositeDecoder
import kotlin.collections.plus

private val PRIMITIVE_SERIAL_TYPES = mapOf(
    Boolean.serializer().descriptor.serialName to typeOf<Boolean>(),
    Boolean.serializer().nullable.descriptor.serialName to typeOf<Boolean?>(),
    UByte.serializer().descriptor.serialName to typeOf<UByte>(),
    UByte.serializer().nullable.descriptor.serialName to typeOf<UByte?>(),
    UShort.serializer().descriptor.serialName to typeOf<UShort>(),
    UShort.serializer().nullable.descriptor.serialName to typeOf<UShort?>(),
    UInt.serializer().descriptor.serialName to typeOf<UInt>(),
    UInt.serializer().nullable.descriptor.serialName to typeOf<UInt?>(),
    ULong.serializer().descriptor.serialName to typeOf<ULong>(),
    ULong.serializer().nullable.descriptor.serialName to typeOf<ULong?>(),
    Byte.serializer().descriptor.serialName to typeOf<Byte>(),
    Byte.serializer().nullable.descriptor.serialName to typeOf<Byte?>(),
    Short.serializer().descriptor.serialName to typeOf<Short>(),
    Short.serializer().nullable.descriptor.serialName to typeOf<Short?>(),
    Int.serializer().descriptor.serialName to typeOf<Int>(),
    Int.serializer().nullable.descriptor.serialName to typeOf<Int?>(),
    Long.serializer().descriptor.serialName to typeOf<Long>(),
    Long.serializer().nullable.descriptor.serialName to typeOf<Long?>(),
    Float.serializer().descriptor.serialName to typeOf<Float>(),
    Float.serializer().nullable.descriptor.serialName to typeOf<Float?>(),
    Double.serializer().descriptor.serialName to typeOf<Double>(),
    Double.serializer().nullable.descriptor.serialName to typeOf<Double?>(),
    BigIntegerSerializer.descriptor.serialName to typeOf<BigInteger>(),
    BigIntegerSerializer.nullable.descriptor.serialName to typeOf<BigInteger?>(),
    BigDecimalSerializer.descriptor.serialName to typeOf<BigDecimal>(),
    BigDecimalSerializer.nullable.descriptor.serialName to typeOf<BigDecimal?>(),
    Char.serializer().descriptor.serialName to typeOf<Char>(),
    Char.serializer().nullable.descriptor.serialName to typeOf<Char?>(),
    String.serializer().descriptor.serialName to typeOf<String>(),
    String.serializer().nullable.descriptor.serialName to typeOf<String?>(),
    LocalTime.serializer().descriptor.serialName to typeOf<LocalTime>(),
    LocalTime.serializer().nullable.descriptor.serialName to typeOf<LocalTime?>(),
    LocalDate.serializer().descriptor.serialName to typeOf<LocalDate>(),
    LocalDate.serializer().nullable.descriptor.serialName to typeOf<LocalDate?>(),
    LocalDateTime.serializer().descriptor.serialName to typeOf<LocalDateTime>(),
    LocalDateTime.serializer().nullable.descriptor.serialName to typeOf<LocalDateTime?>(),
    Uuid.serializer().descriptor.serialName to typeOf<Uuid>(),
    Uuid.serializer().nullable.descriptor.serialName to typeOf<Uuid?>(),
)

public val SerialDescriptor.primitiveTypeOrNull: KType?
    get() = PRIMITIVE_SERIAL_TYPES[serialName]

public val SerialDescriptor.primitiveType: KType
    get() = primitiveTypeOrNull!!

public val SerialDescriptor.classDiscriminator: String
    get() = getElementName(0)

public val SerialDescriptor.valueDiscriminator: String
    get() = getElementName(1)

public inline fun <reified T : Annotation> SerialDescriptor.hasAnnotation(): Boolean =
    annotations.containsInstance<T>()

public inline fun <reified T : Annotation> SerialDescriptor.getAnnotations(): List<T> =
    annotations.filterIsInstance<T>()

public inline fun <reified T : Annotation> SerialDescriptor.getAnnotation(): T? =
    getAnnotations<T>().singleOrNull()

public inline fun <reified T : Annotation> SerialDescriptor.hasElementAnnotation(index: Int): Boolean =
    getElementAnnotations(index).filterIsInstance<T>().isNotEmpty()

public inline fun <reified T : Annotation> SerialDescriptor.getElementAnnotation(index: Int): T? =
    getElementAnnotations(index).filterIsInstance<T>().takeIfNotEmpty()?.single()

public fun SerialDescriptor.getElementIndexOrNull(name: String): Int? =
    getElementIndex(name).let { index ->
        if (index == CompositeDecoder.UNKNOWN_NAME) null else index
    }

public fun SerialDescriptor.getElementDescriptor(name: String): SerialDescriptor? =
    getElementIndexOrNull(name)?.let(::getElementDescriptor)

public inline fun <reified T : Annotation> SerialDescriptor.hasElementAnnotation(name: String): Boolean? =
    getElementIndexOrNull(name)?.let { index -> hasElementAnnotation<T>(index) }

public fun SerialDescriptor.getElementAnnotations(name: String): List<Annotation>? =
    getElementIndexOrNull(name)?.let(::getElementAnnotations)

public fun SerialDescriptor.isElementOptional(name: String): Boolean? =
    getElementIndexOrNull(name)?.let(::isElementOptional)

public fun SerialDescriptor.validatorRules(
    uIntPatternMessage: String = "Value is not unsigned integer",
    intPatternMessage: String = "Value is not integer",
    floatPatternMessage: String = "Value is not float",
): List<ValidatorRule> = listOfNotNull(
    isNullable.takeIf { !it }?.let { ValidatorRule.nonEmpty("value_is_empty") },
    primitiveType.kClass.isUIntNumber.takeIf { it }?.let { ValidatorRule.uIntValue(uIntPatternMessage) },
    primitiveType.kClass.isIntNumber.takeIf { it }?.let { ValidatorRule.intValue(intPatternMessage) },
    primitiveType.kClass.isFloatNumber.takeIf { it }?.let { ValidatorRule.floatValue(floatPatternMessage) },
)

public fun SerialDescriptor.validator(
    type: Validation = Validation.FAIL_FAST,
    additionalRules: List<ValidatorRule> = emptyList(),
    uIntPatternMessage: String = "value_is_not_unsigned_integer",
    intPatternMessage: String = "value_is_not_integer",
    floatPatternMessage: String = "value_is_not_float",
): Validator? = validatorRules(uIntPatternMessage, intPatternMessage, floatPatternMessage).takeIfNotEmpty()
    ?.let { Validator(type, it + additionalRules, !isNullable) }

public fun SerialDescriptor.buildPolymorphicDescriptor(classDiscriminator: String): SerialDescriptor =
    buildSerialDescriptor(serialName, kind) {
        element(this@buildPolymorphicDescriptor, 0, elementName = classDiscriminator)
        element(this@buildPolymorphicDescriptor, 1)
    }

public fun SerialDescriptor.toPolymorphicValues(
    value: Map<String, Any?>,
    classDiscriminator: String = this@toPolymorphicValues.classDiscriminator,
    valueDiscriminator: String = this@toPolymorphicValues.valueDiscriminator,
): List<Any?> {
    require(kind is PolymorphicKind) { "Provided descriptor is not polymorphic: $this" }

    return listOf(
        value[classDiscriminator] ?: throw SerializationException(
            "Class serialName not found with classDiscriminator: $classDiscriminator",
        ),
        value[valueDiscriminator] ?: emptyMap<String, Any?>(),// Empty map if object
    )
}

public fun SerialDescriptor.toPolymorphicMap(
    serialName: String,
    value: Any?,
    classDiscriminator: String = this@toPolymorphicMap.classDiscriminator,
    valueDiscriminator: String = this@toPolymorphicMap.valueDiscriminator,
): Map<String, Any?> {
    require(kind is PolymorphicKind) { "Provided descriptor is not polymorphic: $this" }

    return mapOf(
        classDiscriminator to serialName,
        valueDiscriminator to value,
    )
}

@Suppress("UNCHECKED_CAST")
public fun <T : Any> SerialDescriptor.unknownKeysOf(value: T): T =
    value.deepMinusKeys<T, Pair<Any?, SerialDescriptor>>(
        *elementNames.zip(elementDescriptors).toTypedArray(),
        sourcePathKey = { sourcePath -> sourcePath.first },
        sourcePathChildren = { value, (_, descriptor) ->
            when (descriptor.kind) {
                StructureKind.LIST -> value.asList.indices.map { index ->
                    index to descriptor.getElementDescriptor(0)
                }

                StructureKind.MAP -> value.asMap<Any?, Any?>().map { (key, _) ->
                    key to descriptor.getElementDescriptor(1)
                }

                StructureKind.CLASS -> descriptor.elementNames.zip(descriptor.elementDescriptors)

                else -> emptyList()
            }
        }
    )
