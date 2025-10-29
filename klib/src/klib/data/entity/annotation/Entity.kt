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
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.reflect.KClass
import kotlin.reflect.typeOf
import kotlin.text.isEmpty
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
public annotation class Entity(val name: String = "")

public fun KClass<*>.getEntityProperties(): BiMap<String, String> =
    serializer().descriptor.let { descriptor ->
        descriptor.elementIndices.filter { index ->
            !descriptor.hasElementAnnotation<Transient>(index)
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
    boolean: T.(propertyName: String, defaultValue: Boolean?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    uByte: T.(propertyName: String, defaultValue: UByte?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    uShort: T.(propertyName: String, defaultValue: UShort?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    uInt: T.(propertyName: String, defaultValue: UInt?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    uLong: T.(propertyName: String, defaultValue: ULong?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    byte: T.(propertyName: String, defaultValue: Byte?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    short: T.(propertyName: String, defaultValue: Short?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    int: T.(propertyName: String, defaultValue: Int?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    long: T.(propertyName: String, defaultValue: Long?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    float: T.(propertyName: String, defaultValue: Float?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    double: T.(propertyName: String, defaultValue: Double?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    decimal: T.(
        propertyName: String,
        precision: Long,
        scale: Long,
        defaultValue: BigDecimal?
    ) -> C = { _, _, _, _ ->
        throw UnsupportedOperationException()
    },
    char: T.(
        propertyName: String,
        length: Int,
        collate: String?,
        defaultValue: String?
    ) -> C = { _, _, _, _ ->
        throw UnsupportedOperationException()
    },
    varchar: T.(
        propertyName: String,
        length: Int,
        collate: String?,
        defaultValue: String?
    ) -> C = { _, _, _, _ ->
        throw UnsupportedOperationException()
    },
    text: T.(
        propertyName: String,
        collate: String?,
        eagerLoading: Boolean,
        defaultValue: String?
    ) -> C = { _, _, _, _ ->
        throw UnsupportedOperationException()
    },
    duration: T.(propertyName: String, defaultValue: Duration?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    instant: T.(propertyName: String, defaultValue: Instant?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    time: T.(propertyName: String, defaultValue: LocalTime?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    date: T.(propertyName: String, defaultValue: LocalDate?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    datetime: T.(propertyName: String, defaultValue: LocalDateTime?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    uuid: T.(propertyName: String, defaultValue: Uuid?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    enum: T.(
        propertyName: String,
        length: Int?,
        propertyDescriptor: SerialDescriptor
    ) -> C = { _, _, _ ->
        throw UnsupportedOperationException()
    },
    uByteArray: T.(propertyName: String, defaultValue: List<UByte>?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    uShortArray: T.(propertyName: String, defaultValue: List<UShort>?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    uIntArray: T.(propertyName: String, defaultValue: List<UInt>?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    uLongArray: T.(propertyName: String, defaultValue: List<ULong>?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    binary: T.(propertyName: String, length: Int?, defaultValue: ByteArray?) -> C = { _, _, _ ->
        throw UnsupportedOperationException()
    },
    blob: T.(
        propertyName: String,
        useObjectIdentifier: Boolean,
        defaultValue: ByteArray?
    ) -> C = { _, _, _ ->
        throw UnsupportedOperationException()
    },
    shortArray: T.(propertyName: String, defaultValue: List<Short>?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    intArray: T.(propertyName: String, defaultValue: List<Int>?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    longArray: T.(propertyName: String, defaultValue: List<Long>?) -> C = { _, _ ->
        throw UnsupportedOperationException()
    },
    json: T.(propertyName: String, KSerializer<Any>, defaultValue: String?) -> C = { _, _, _ ->
        throw UnsupportedOperationException()
    },
    jsonb: T.(propertyName: String, KSerializer<Any>, defaultValue: String?) -> C = { _, _, _ ->
        throw UnsupportedOperationException()
    },
    nullable: T.(property: C) -> C = { throw UnsupportedOperationException() },
    autoIncrement: T.(property: C, seqName: String?) -> C = { _, _ -> throw UnsupportedOperationException() },
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
    val serializer = serializer()
    val descriptor = serializer().descriptor

    val entity = table(
        descriptor.getAnnotation<Entity>()?.name?.takeUnless(String::isEmpty)
            ?: descriptor.serialName.toSnakeCase(),
    )

    val properties = mutableMapOf<String, C>()

    descriptor.elementIndices.filterNot { index ->
        descriptor.hasElementAnnotation<Transient>(index)
    }.forEach { index ->
        val elementDescriptor = descriptor.getElementDescriptor(index)
        val property = descriptor.getElementAnnotation<Property>(index)
        val propertyName = property?.name ?: descriptor.getElementName(index)
        val defaultValue = descriptor.getElementAnnotation<Default>(index)?.value

        properties += propertyName to if (descriptor.hasElementAnnotation<Json>(index))
            entity.json(
                propertyName,
                serializer.childSerializer(index) as KSerializer<Any>,
                defaultValue,
            )
        else if (descriptor.hasElementAnnotation<Jsonb>(index))
            entity.jsonb(
                propertyName,
                serializer.childSerializer(index) as KSerializer<Any>,
                defaultValue,
            )
        else if (elementDescriptor.isEnum) entity.enum(
            propertyName,
            descriptor.getElementAnnotation<Enum>(index)?.length,
            elementDescriptor,
        )
        else when (elementDescriptor.primitiveTypeOrNull?.withNullability(false)) {
            typeOf<Boolean>() -> entity.boolean(propertyName, defaultValue?.toBoolean())
            typeOf<UByte>() -> entity.uByte(propertyName, defaultValue?.toUByte())
            typeOf<UShort>() -> entity.uShort(propertyName, defaultValue?.toUShort())
            typeOf<UInt>() -> entity.uInt(propertyName, defaultValue?.toUInt())
            typeOf<ULong>() -> entity.uLong(propertyName, defaultValue?.toULong())
            typeOf<Byte>() -> entity.byte(propertyName, defaultValue?.toByte())
            typeOf<Short>() -> entity.short(propertyName, defaultValue?.toShort())
            typeOf<Int>() -> entity.int(propertyName, defaultValue?.toInt())
            typeOf<Long>() -> entity.long(propertyName, defaultValue?.toLong())
            typeOf<Float>() -> entity.float(propertyName, defaultValue?.toFloat())
            typeOf<Double>() -> entity.double(propertyName, defaultValue?.toDouble())
            typeOf<BigDecimal>() ->
                descriptor.getElementAnnotation<Decimal>(index)?.let { annotation ->
                    entity.decimal(
                        propertyName,
                        annotation.precision,
                        annotation.scale,
                        defaultValue?.toBigDecimal(),
                    )
                } ?: error("Missing decimal annotation for column '$propertyName'")

            typeOf<String>() ->
                descriptor.getElementAnnotation<Char>(index)?.let { annotation ->
                    entity.char(
                        propertyName,
                        annotation.length,
                        annotation.collate.takeUnless(String::isEmpty),
                        defaultValue,
                    )
                } ?: descriptor.getElementAnnotation<Varchar>(index)?.let { annotation ->
                    entity.varchar(
                        propertyName,
                        annotation.length,
                        annotation.collate.takeUnless(String::isEmpty),
                        defaultValue,
                    )
                } ?: descriptor.getElementAnnotation<Text>(index)?.let { annotation ->
                    entity.text(
                        propertyName,
                        annotation.collate,
                        annotation.eagerLoading,
                        defaultValue,
                    )
                } ?: error("Missing string annotation for column '$propertyName'")

            typeOf<Duration>() -> entity.duration(
                propertyName,
                defaultValue?.toDuration(),
            )

            typeOf<Instant>() -> entity.instant(propertyName, defaultValue?.toInstant())
            typeOf<LocalTime>() -> entity.time(propertyName, defaultValue?.toLocalTime())
            typeOf<LocalDate>() -> entity.date(propertyName, defaultValue?.toLocalDate())
            typeOf<LocalDateTime>() -> entity.datetime(
                propertyName,
                defaultValue?.toLocalDateTime(),
            )

            typeOf<Uuid>() -> entity.uuid(propertyName, defaultValue?.toUuid())

            else -> when (elementDescriptor.serialName) {
                UByteArray::class.serializer().descriptor.serialName -> entity.uByteArray(
                    propertyName,
                    defaultValue?.split(",")?.map(String::toUByte),
                )

                UShortArray::class.serializer().descriptor.serialName -> entity.uShortArray(
                    propertyName,
                    defaultValue?.split(",")?.map(String::toUShort),
                )

                UIntArray::class.serializer().descriptor.serialName -> entity.uIntArray(
                    propertyName,
                    defaultValue?.split(",")?.map(String::toUInt),
                )

                ULongArray::class.serializer().descriptor.serialName -> entity.uLongArray(
                    propertyName,
                    defaultValue?.split(",")?.map(String::toULong),
                )

                ByteArray::class.serializer().descriptor.serialName ->
                    descriptor.getElementAnnotation<Binary>(index)?.let { annotation ->
                        entity.binary(
                            propertyName,
                            annotation.length,
                            defaultValue?.encodeToByteArray(),
                        )
                    } ?: descriptor.getElementAnnotation<Blob>(index)?.let { annotation ->
                        entity.blob(
                            propertyName,
                            annotation.useObjectIdentifier,
                            defaultValue?.encodeToByteArray(),
                        )
                    } ?: error("Missing bytearray column annotation")

                ShortArray::class.serializer().descriptor.serialName -> entity.shortArray(
                    propertyName,
                    defaultValue?.split(",")?.map(String::toShort),
                )

                IntArray::class.serializer().descriptor.serialName -> entity.intArray(
                    propertyName,
                    defaultValue?.split(",")?.map(String::toInt),
                )

                LongArray::class.serializer().descriptor.serialName -> entity.longArray(
                    propertyName,
                    defaultValue?.split(",")?.map(String::toLong),
                )

                else -> entity.json(
                    propertyName,
                    serializer.childSerializer(index) as KSerializer<Any>,
                    defaultValue,
                )
            }
        }

        if (elementDescriptor.isNullable &&
            descriptor.getAnnotation<PrimaryKey>()?.properties?.contains(propertyName) != true &&
            !descriptor.hasElementAnnotation<PrimaryKey>(index) &&
            !descriptor.hasElementAnnotation<AutoIncrement>(index) &&
            !descriptor.hasElementAnnotation<DatabaseGenerated>(index)
        ) entity.nullable(properties[propertyName]!!)

        descriptor.getElementAnnotation<AutoIncrement>(index)?.let { annotation ->
            entity.autoIncrement(properties[propertyName]!!, annotation.seqName.takeUnlessEmpty())
        }

        descriptor.getElementAnnotation<DatabaseGenerated>(index)?.let { annotation ->
            entity.databaseGenerated(properties[propertyName]!!)
        }

        descriptor.getElementAnnotation<Index>(index)?.let { annotation ->
            entity.index(
                annotation.indexName.takeUnless(String::isEmpty),
                annotation.indexType.takeUnless(String::isEmpty),
                annotation.isUnique,
                listOf(properties[propertyName]!!),
            )
        }

        descriptor.getElementAnnotation<PrimaryKey>(index)?.let { annotation ->
            entity.primaryKey(
                annotation.name.takeUnless(String::isEmpty),
                listOf(properties[propertyName]!!),
            )
        }

        descriptor.getElementAnnotation<FkReference>(index)?.let { annotation ->
            val foreignKey = descriptor.getElementAnnotation<ForeignKey>(index)

            entity.foreignKey(
                (foreignKey?.name ?: annotation.name).takeUnless(String::isEmpty),
                listOf(
                    Tuple3(
                        properties[propertyName]!!,
                        annotation.targetEntity,
                        annotation.targetProperty,
                    ),
                ),
                foreignKey?.onUpdate?.takeUnless(String::isEmpty),
                foreignKey?.onDelete?.takeUnless(String::isEmpty),
            )
        }
    }

    descriptor.getAnnotations<Index>().forEach { annotation ->
        entity.index(
            annotation.indexName.takeUnless(String::isEmpty),
            annotation.indexType.takeUnless(String::isEmpty),
            annotation.isUnique,
            annotation.properties.map { propertyName -> properties[propertyName]!! },
        )
    }

    descriptor.getAnnotation<PrimaryKey>()?.let { annotation ->
        entity.primaryKey(
            annotation.name.takeUnless(String::isEmpty),
            annotation.properties.map { propertyName -> properties[propertyName]!! },
        )
    }

    descriptor.getAnnotations<FkReference>()
        .groupBy(FkReference::name)
        .forEach { (name, annotations) ->
            val foreignKey = descriptor.getAnnotations<ForeignKey>()
                .find { annotation -> annotation.name == name }

            entity.foreignKey(
                name.takeUnless(String::isEmpty),
                annotations.map { annotation ->
                    Tuple3(
                        properties[annotation.property]!!,
                        annotation.targetEntity,
                        annotation.targetProperty,
                    )
                },
                foreignKey?.onUpdate?.takeUnless(String::isEmpty),
                foreignKey?.onDelete?.takeUnless(String::isEmpty),
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
