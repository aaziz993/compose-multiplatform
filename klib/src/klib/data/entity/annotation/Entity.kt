package klib.data.entity.annotation

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import klib.data.type.collections.bimap.BiMap
import klib.data.type.collections.bimap.toBiMap
import klib.data.type.primitives.string.case.toSnakeCase
import klib.data.type.primitives.string.takeUnlessEmpty
import klib.data.type.primitives.time.toDuration
import klib.data.type.primitives.time.toInstant
import klib.data.type.primitives.time.toLocalDate
import klib.data.type.primitives.time.toLocalDateTime
import klib.data.type.primitives.time.toLocalTime
import klib.data.type.primitives.toUuid
import klib.data.type.reflection.withNullability
import klib.data.type.serialization.childSerializer
import klib.data.type.serialization.elementIndices
import klib.data.type.serialization.getAnnotation
import klib.data.type.serialization.getAnnotations
import klib.data.type.serialization.getElementAnnotation
import klib.data.type.serialization.hasElementAnnotation
import klib.data.type.serialization.isEnum
import klib.data.type.serialization.primitiveTypeOrNull
import klib.data.type.tuples.Tuple3
import kotlin.reflect.KClass
import kotlin.reflect.typeOf
import kotlin.time.Duration
import kotlin.time.Instant
import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialInfo
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.serializer

@SerialInfo
@Target(AnnotationTarget.CLASS)
public annotation class Entity(
    val name: String = "",
    val implicitProperties: Boolean = true,
)

public fun KClass<*>.getEntityProperties(): BiMap<String, String> =
    serializer().descriptor.let { descriptor ->
        val entityAnnotation = descriptor.getAnnotation<Entity>()
            ?: error("Missing Entity annotation on '$simpleName'")
        descriptor.elementIndices.filter { index ->
            !descriptor.hasElementAnnotation<TransientProperty>(index) &&
                (descriptor.hasElementAnnotation<Property>(index) ||
                    entityAnnotation.implicitProperties)
        }.associate { index ->
            descriptor.getElementName(index).let { elementName ->
                elementName to (descriptor.getElementAnnotation<Property>(index)?.name
                    ?: elementName)
            }
        }.toBiMap()
    }

public fun KClass<*>.getEntityGeneratedProperties(): List<String> =
    annotatedProperties<AutoIncrement>() + annotatedProperties<DatabaseGenerated>()

public fun KClass<*>.getEntityIdProperties(): List<String> =
    serializer().descriptor.getAnnotation<PrimaryKey>()?.properties?.toList()
        ?: listOfNotNull(annotatedProperties<PrimaryKey>().firstOrNull())

public fun KClass<*>.getEntityCreatedAtProperty(): String? =
    annotatedProperties<CreatedAt>().firstOrNull()

public fun KClass<*>.getEntityUpdatedAtProperty(): String? =
    annotatedProperties<UpdatedAt>().firstOrNull()

@Suppress("UNCHECKED_CAST")
public inline fun <T : Any, C : Any> KClass<*>.toTable(
    table: (name: String) -> T,
    boolean: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    uByte: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    uShort: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    uInt: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    uLong: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    byte: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    short: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    int: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    long: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    float: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    double: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    decimal: T.(
        propertyName: String,
        precision: Long,
        scale: Long,
    ) -> C = { _, _, _ -> throw UnsupportedOperationException() },
    char: T.(
        propertyName: String,
        length: Int,
        collate: String?,
    ) -> C = { _, _, _ -> throw UnsupportedOperationException() },
    varchar: T.(
        propertyName: String,
        length: Int,
        collate: String?,
    ) -> C = { _, _, _ -> throw UnsupportedOperationException() },
    text: T.(
        propertyName: String,
        collate: String?,
        eagerLoading: Boolean,
    ) -> C = { _, _, _ -> throw UnsupportedOperationException() },
    duration: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    instant: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    time: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    date: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    datetime: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    uuid: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    enum: T.(
        propertyName: String,
        length: Int?,
        propertyDescriptor: SerialDescriptor,
    ) -> C = { _, _, _ -> throw UnsupportedOperationException() },
    uByteArray: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    uShortArray: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    uIntArray: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    uLongArray: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    binary: T.(propertyName: String, length: Int?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    blob: T.(propertyName: String, useObjectIdentifier: Boolean) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    shortArray: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    intArray: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    longArray: T.(propertyName: String) -> C = { throw UnsupportedOperationException() },
    json: T.(propertyName: String, KSerializer<Any>) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    jsonb: T.(propertyName: String, KSerializer<Any>) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    nullable: T.(property: C) -> C = { throw UnsupportedOperationException() },
    crossinline enumDefaultValue: (propertyDescriptor: SerialDescriptor, value: String) -> Any = { _, _ ->
        throw UnsupportedOperationException()
    },
    clientDefault: T.(property: C, value: Any?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    default: T.(property: C, value: Any?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    autoIncrement: T.(property: C, seqName: String?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    autoGenerate: T.(property: C) -> C = { throw UnsupportedOperationException() },
    databaseGenerated: T.(property: C) -> C = { throw UnsupportedOperationException() },
    index: T.(
        indexName: String?,
        indexType: String?,
        isUnique: Boolean,
        columns: List<C>
    ) -> Unit = { _, _, _, _ ->
        throw UnsupportedOperationException()
    },
    primaryKey: T.(name: String?, columns: List<C>) -> Unit = { _, _ ->
        throw UnsupportedOperationException()
    },
    foreignKey: T. (
        name: String?,
        references: List<Tuple3<C, String, String>>,
        onUpdate: String?,
        onDelete: String?
    ) -> Unit = { _, _, _, _ ->
        throw UnsupportedOperationException()
    },
): Pair<T, Map<String, C>> {
    val entitySerializer = serializer()
    val entityDescriptor = serializer().descriptor
    val entityAnnotation = entityDescriptor.getAnnotation<Entity>()
        ?: error("Missing Entity annotation on '$simpleName'")
    val entity =
        table(entityAnnotation.name.takeUnlessEmpty() ?: entityDescriptor.serialName.toSnakeCase())
    val properties = mutableMapOf<String, C>()

    entityDescriptor.elementIndices.filter { index ->
        !entityDescriptor.hasElementAnnotation<TransientProperty>(index) &&
            (entityDescriptor.hasElementAnnotation<Property>(index) ||
                entityAnnotation.implicitProperties)
    }.forEach { index ->
        val elementDescriptor = entityDescriptor.getElementDescriptor(index)
        val childSerializer = entitySerializer.childSerializer(index) as KSerializer<Any>
        val propertyAnnotation = entityDescriptor.getElementAnnotation<Property>(index)
        val propertyName =
            propertyAnnotation?.name?.takeUnlessEmpty() ?: entityDescriptor.getElementName(index)
        val nullable =
            !entityDescriptor.hasElementAnnotation<NotNull>(index) && elementDescriptor.isNullable

        val propertyDefaultFn: Pair<C, (String) -> Any> =
            if (entityDescriptor.hasElementAnnotation<Json>(index))
                entity.json(
                    propertyName,
                    childSerializer,
                ) to { value ->
                    kotlinx.serialization.json.Json.Default.decodeFromString(childSerializer, value)
                }
            else if (entityDescriptor.hasElementAnnotation<Jsonb>(index))
                entity.jsonb(
                    propertyName,
                    entitySerializer.childSerializer(index) as KSerializer<Any>,
                ) to { value ->
                    kotlinx.serialization.json.Json.Default.decodeFromString(childSerializer, value)
                }
            else if (elementDescriptor.isEnum) entity.enum(
                propertyName,
                entityDescriptor.getElementAnnotation<Enum>(index)?.length,
                elementDescriptor,
            ) to { value -> enumDefaultValue(elementDescriptor, value) }
            else when (elementDescriptor.primitiveTypeOrNull?.withNullability(false)) {
                typeOf<Boolean>() -> entity.boolean(propertyName) to { value -> value.toBoolean() }
                typeOf<UByte>() -> entity.uByte(propertyName) to { value -> value.toUByte() }
                typeOf<UShort>() -> entity.uShort(propertyName) to { value -> value.toUShort() }
                typeOf<UInt>() -> entity.uInt(propertyName) to { value -> value.toUInt() }
                typeOf<ULong>() -> entity.uLong(propertyName) to { value -> value.toULong() }
                typeOf<Byte>() -> entity.byte(propertyName) to { value -> value.toByte() }
                typeOf<Short>() -> entity.short(propertyName) to { value -> value.toShort() }
                typeOf<Int>() -> entity.int(propertyName) to { value -> value.toInt() }
                typeOf<Long>() -> entity.long(propertyName) to { value -> value.toLong() }
                typeOf<Float>() -> entity.float(propertyName) to { value -> value.toFloat() }
                typeOf<Double>() -> entity.double(propertyName) to { value -> value.toDouble() }
                typeOf<BigDecimal>() -> {
                    val (precision, scale) = entityDescriptor.getElementAnnotation<Decimal>(index)
                        ?.let { annotation ->
                            annotation.precision to annotation.scale
                        } ?: (16L to 20L)

                    entity.decimal(
                        propertyName,
                        precision,
                        scale
                    ) to { value -> value.toBigDecimal() }
                }

                typeOf<String>() ->
                    (entityDescriptor.getElementAnnotation<Char>(index)?.let { annotation ->
                        entity.char(
                            propertyName,
                            annotation.length,
                            annotation.collate.takeUnlessEmpty(),
                        )
                    } ?: entityDescriptor.getElementAnnotation<Varchar>(index)?.let { annotation ->
                        entity.varchar(
                            propertyName,
                            annotation.length,
                            annotation.collate.takeUnlessEmpty(),
                        )
                    } ?: entityDescriptor.getElementAnnotation<Text>(index)?.let { annotation ->
                        entity.text(
                            propertyName,
                            annotation.collate.takeUnlessEmpty(),
                            annotation.eagerLoading,
                        )
                    } ?: entity.varchar(propertyName, 255, null)) to { value -> value }

                typeOf<Duration>() -> entity.duration(propertyName) to { value -> value.toDuration() }
                typeOf<Instant>() -> entity.instant(propertyName) to { value -> value.toInstant() }
                typeOf<LocalTime>() -> entity.time(propertyName) to { value -> value.toLocalTime() }
                typeOf<LocalDate>() -> entity.date(propertyName) to { value -> value.toLocalDate() }
                typeOf<LocalDateTime>() -> entity.datetime(propertyName) to { value ->
                    value.toLocalDateTime()
                }

                typeOf<Uuid>() -> {
                    entity.uuid(propertyName).let { property ->
                        if (entityDescriptor.hasElementAnnotation<AutoGenerate>(index))
                            entity.autoGenerate(property) else property
                    } to { value -> value.toUuid() }
                }

                else -> when (elementDescriptor.serialName) {
                    UByteArray::class.serializer().descriptor.serialName ->
                        entity.uByteArray(propertyName) to { value ->
                            value.split(",").map(String::toUByte)
                        }

                    UShortArray::class.serializer().descriptor.serialName ->
                        entity.uShortArray(propertyName) to { value ->
                            value.split(",").map(String::toUShort)
                        }

                    UIntArray::class.serializer().descriptor.serialName ->
                        entity.uIntArray(propertyName) to { value ->
                            value.split(",").map(String::toUInt)
                        }

                    ULongArray::class.serializer().descriptor.serialName ->
                        entity.uLongArray(propertyName) to { value ->
                            value.split(",").map(String::toULong)
                        }

                    ByteArray::class.serializer().descriptor.serialName ->
                        (entityDescriptor.getElementAnnotation<Binary>(index)?.let { annotation ->
                            entity.binary(
                                propertyName,
                                annotation.length.takeIf { length -> length > -1 },
                            )
                        } ?: entityDescriptor.getElementAnnotation<Blob>(index)?.let { annotation ->
                            entity.blob(propertyName, annotation.useObjectIdentifier)
                        } ?: entity.binary(propertyName, null)) to { value ->
                            value.encodeToByteArray()
                        }

                    ShortArray::class.serializer().descriptor.serialName ->
                        entity.shortArray(propertyName) to { value ->
                            value.split(",").map(String::toShort)
                        }

                    IntArray::class.serializer().descriptor.serialName ->
                        entity.intArray(propertyName) to { value ->
                            value.split(",").map(String::toInt)
                        }

                    LongArray::class.serializer().descriptor.serialName ->
                        entity.longArray(propertyName) to { value ->
                            value.split(",").map(String::toLong)
                        }

                    else -> entity.json(
                        propertyName,
                        entitySerializer.childSerializer(index) as KSerializer<Any>,
                    ) to { value ->
                        kotlinx.serialization.json.Json.Default.decodeFromString(
                            childSerializer,
                            value
                        )
                    }
                }
            }

        properties += propertyName to propertyDefaultFn.first

        if (nullable &&
            entityDescriptor.getAnnotation<PrimaryKey>()?.properties?.contains(propertyName) != true &&
            !entityDescriptor.hasElementAnnotation<PrimaryKey>(index) &&
            !entityDescriptor.hasElementAnnotation<AutoIncrement>(index) &&
            !entityDescriptor.hasElementAnnotation<DatabaseGenerated>(index)
        ) properties += propertyName to entity.nullable(properties[propertyName]!!)

        entityDescriptor.getAnnotation<ClientDefault>()?.let { annotation ->
            properties += propertyName to entity.clientDefault(
                properties[propertyName]!!,
                annotation.value.takeUnlessEmpty()?.let(propertyDefaultFn.second)
            )
        }

        entityDescriptor.getAnnotation<DefaultValue>()?.let { annotation ->
            properties += propertyName to entity.default(
                properties[propertyName]!!,
                annotation.value.takeUnlessEmpty()?.let(propertyDefaultFn.second)
            )
        }

        entityDescriptor.getElementAnnotation<AutoIncrement>(index)?.let { annotation ->
            properties += propertyName to entity.autoIncrement(
                properties[propertyName]!!,
                annotation.seqName.takeUnlessEmpty(),
            )
        }

        entityDescriptor.getElementAnnotation<DatabaseGenerated>(index)?.let { annotation ->
            properties += propertyName to entity.databaseGenerated(properties[propertyName]!!)
        }

        entityDescriptor.getElementAnnotation<Index>(index)?.let { annotation ->
            entity.index(
                annotation.indexName.takeUnlessEmpty(),
                annotation.indexType.takeUnlessEmpty(),
                annotation.isUnique,
                listOf(properties[propertyName]!!),
            )
        }

        entityDescriptor.getElementAnnotation<PrimaryKey>(index)?.let { annotation ->
            entity.primaryKey(
                annotation.name.takeUnlessEmpty(),
                listOf(properties[propertyName]!!),
            )
        }

        entityDescriptor.getElementAnnotation<FkReference>(index)?.let { annotation ->
            val foreignKey = entityDescriptor.getElementAnnotation<ForeignKey>(index)

            entity.foreignKey(
                (foreignKey?.name ?: annotation.name).takeUnlessEmpty(),
                listOf(
                    Tuple3(
                        properties[propertyName]!!,
                        annotation.targetEntity,
                        annotation.targetProperty,
                    ),
                ),
                foreignKey?.onUpdate?.takeUnlessEmpty(),
                foreignKey?.onDelete?.takeUnlessEmpty(),
            )
        }
    }

    entityDescriptor.getAnnotations<Index>().forEach { annotation ->
        entity.index(
            annotation.indexName.takeUnlessEmpty(),
            annotation.indexType.takeUnlessEmpty(),
            annotation.isUnique,
            annotation.properties.map { propertyName -> properties[propertyName]!! },
        )
    }

    entityDescriptor.getAnnotation<PrimaryKey>()?.let { annotation ->
        entity.primaryKey(
            annotation.name.takeUnlessEmpty(),
            annotation.properties.map { propertyName -> properties[propertyName]!! },
        )
    }

    entityDescriptor.getAnnotations<FkReference>()
        .groupBy(FkReference::name)
        .forEach { (name, annotations) ->
            val foreignKey = entityDescriptor.getAnnotations<ForeignKey>()
                .find { annotation -> annotation.name == name }

            entity.foreignKey(
                name.takeUnlessEmpty(),
                annotations.map { annotation ->
                    Tuple3(
                        properties[annotation.property]!!,
                        annotation.targetEntity,
                        annotation.targetProperty,
                    )
                },
                foreignKey?.onUpdate?.takeUnlessEmpty(),
                foreignKey?.onDelete?.takeUnlessEmpty(),
            )
        }

    return entity to properties
}

private inline fun <reified T : Annotation> KClass<*>.annotatedProperties(): List<String> =
    serializer().descriptor.let { descriptor ->
        descriptor.elementIndices.filter { index -> descriptor.hasElementAnnotation<T>(index) }
            .map { index ->
                descriptor.getElementAnnotation<Property>(index)?.name
                    ?: descriptor.getElementName(index)
            }
    }
