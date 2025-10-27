package klib.data.entity.annotation

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import klib.data.type.collections.map.getStrict
import klib.data.type.collections.sortedByReferences
import klib.data.type.primitives.string.case.toSnakeCase
import klib.data.type.primitives.string.takeUnlessEmpty
import klib.data.type.primitives.time.toDuration
import klib.data.type.primitives.time.toInstant
import klib.data.type.primitives.time.toLocalDate
import klib.data.type.primitives.time.toLocalDateTime
import klib.data.type.primitives.time.toLocalTime
import klib.data.type.primitives.toUuid
import klib.data.type.reflection.annotatedTypes
import klib.data.type.reflection.toClass
import klib.data.type.reflection.toKClass
import klib.data.type.serialization.childSerializer
import klib.data.type.serialization.elementIndices
import klib.data.type.serialization.getAnnotation
import klib.data.type.serialization.getAnnotations
import klib.data.type.serialization.getElementAnnotation
import klib.data.type.serialization.hasElementAnnotation
import klib.data.type.serialization.isEnum
import klib.data.type.serialization.primitiveTypeOrNull
import klib.data.type.tuples.Tuple3
import kotlin.collections.contains
import kotlin.reflect.KClass
import kotlin.reflect.full.withNullability
import kotlin.reflect.typeOf
import kotlin.text.isEmpty
import kotlin.time.Duration
import kotlin.time.Instant
import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import org.reflections.util.FilterBuilder

@Suppress("UNCHECKED_CAST")
public fun getEntities(vararg packages: String): List<KClass<*>> =
    Entity::class.annotatedTypes {
        forPackages(*packages)
            .filterInputsBy(
                FilterBuilder().apply {
                    packages.forEach(::includePackage)
                },
            )
    }.sortedByFkReferences()

@Suppress("UNCHECKED_CAST")
private fun List<KClass<*>>.sortedByFkReferences(): List<KClass<*>> =
    sortedByReferences { entities ->
        val descriptor = serializer().descriptor
        (descriptor.getAnnotations<FkReference>()
            .map { reference ->
                reference.targetEntity
            } + descriptor.elementIndices.mapNotNull { index ->
            descriptor.getElementAnnotation<FkReference>(index)?.targetEntity
        }).map(String::toKClass)
    }

@Suppress("UNCHECKED_CAST")
public inline fun <T : Any, C : Any> KClass<*>.toTable(
    table: (name: String) -> T,
    json: T.(propertyName: String, KSerializer<Any>, defaultValue: String?) -> C,
    jsonb: T.(propertyName: String, KSerializer<Any>, defaultValue: String?) -> C,
    boolean: T.(propertyName: String, defaultValue: Boolean?) -> C,
    uByte: T.(propertyName: String, defaultValue: UByte?) -> C,
    uShort: T.(propertyName: String, defaultValue: UShort?) -> C,
    uInt: T.(propertyName: String, defaultValue: UInt?) -> C,
    uLong: T.(propertyName: String, defaultValue: ULong?) -> C,
    byte: T.(propertyName: String, defaultValue: Byte?) -> C,
    short: T.(propertyName: String, defaultValue: Short?) -> C,
    int: T.(propertyName: String, defaultValue: Int?) -> C,
    long: T.(propertyName: String, defaultValue: Long?) -> C,
    float: T.(propertyName: String, defaultValue: Float?) -> C,
    double: T.(propertyName: String, defaultValue: Double?) -> C,
    decimal: T.(propertyName: String, precision: Long, scale: Long, defaultValue: BigDecimal?) -> C,
    char: T.(propertyName: String, length: Int, collate: String?, defaultValue: String?) -> C,
    varchar: T.(propertyName: String, length: Int, collate: String?, defaultValue: String?) -> C,
    text: T.(propertyName: String, collate: String?, eagerLoading: Boolean, defaultValue: String?) -> C,
    duration: T.(propertyName: String, defaultValue: Duration?) -> C,
    instant: T.(propertyName: String, defaultValue: Instant?) -> C,
    time: T.(propertyName: String, defaultValue: LocalTime?) -> C,
    date: T.(propertyName: String, defaultValue: LocalDate?) -> C,
    datetime: T.(propertyName: String, defaultValue: LocalDateTime?) -> C,
    uuid: T.(propertyName: String, defaultValue: Uuid?) -> C,
    enum: T.(propertyName: String, length: Int?, kClass: KClass<out kotlin.Enum<*>>) -> C,
    uByteArray: T.(propertyName: String, defaultValue: List<UByte>?) -> C,
    uShortArray: T.(propertyName: String, defaultValue: List<UShort>?) -> C,
    uIntArray: T.(propertyName: String, defaultValue: List<UInt>?) -> C,
    uLongArray: T.(propertyName: String, defaultValue: List<ULong>?) -> C,
    binary: T.(propertyName: String, length: Int?, defaultValue: ByteArray?) -> C,
    blob: T.(propertyName: String, useObjectIdentifier: Boolean, defaultValue: ByteArray?) -> C,
    shortArray: T.(propertyName: String, defaultValue: List<Short>?) -> C,
    intArray: T.(propertyName: String, defaultValue: List<Int>?) -> C,
    longArray: T.(propertyName: String, defaultValue: List<Long>?) -> C,
    nullable: T.(property: C) -> C,
    autoIncrement: T.(property: C) -> C,
    databaseGenerated: T.(property: C) -> C,
    index: T.(indexName: String?, indexType: String?, isUnique: Boolean, columns: List<C>) -> Unit,
    primaryKey: T.(name: String?, columns: List<C>) -> Unit,
    foreignKey: T. (name: String?, references: List<Tuple3<C, KClass<Any>, String>>, onUpdate: String?, onDelete: String?) -> Unit,
): Pair<T, Map<String, C>> {
    val serializer = serializer()
    val descriptor = serializer().descriptor

    val entity = table(
            descriptor.getAnnotation<Entity>()?.name?.takeUnlessEmpty()
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
                elementDescriptor.serialName.toClass().kotlin as KClass<out kotlin.Enum<*>>,
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
                            annotation.collate.takeUnlessEmpty(),
                            defaultValue,
                    )
                } ?: descriptor.getElementAnnotation<Varchar>(index)?.let { annotation ->
                    entity.varchar(
                            propertyName,
                            annotation.length,
                            annotation.collate.takeUnlessEmpty(),
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
            entity.autoIncrement(properties[propertyName]!!)
        }

        descriptor.getElementAnnotation<DatabaseGenerated>(index)?.let { annotation ->
            entity.databaseGenerated(properties[propertyName]!!)
        }

        descriptor.getElementAnnotation<Index>(index)?.let { annotation ->
            entity.index(
                    annotation.indexName.takeUnlessEmpty(),
                    annotation.indexType.takeUnlessEmpty(),
                    annotation.isUnique,
                    listOf(properties[propertyName]!!),
            )
        }

        descriptor.getElementAnnotation<PrimaryKey>(index)?.let { annotation ->
            entity.primaryKey(
                annotation.name.takeUnlessEmpty(),
                listOf(properties[propertyName]!!),
            )
        }

        descriptor.getElementAnnotation<FkReference>(index)?.let { annotation ->
            val foreignKey = descriptor.getElementAnnotation<ForeignKey>(index)

            entity.foreignKey(
                (foreignKey?.name ?: annotation.name).takeUnlessEmpty(),
                listOf(
                        Tuple3(
                                properties[propertyName]!!,
                                annotation.targetEntity.toKClass() as KClass<Any>,
                                annotation.targetProperty,
                        ),
                ),
                foreignKey?.onUpdate?.takeUnlessEmpty(),
                foreignKey?.onDelete?.takeUnlessEmpty(),
            )
        }
    }

    descriptor.getAnnotations<Index>().forEach { annotation ->
        entity.index(
                annotation.indexName.takeUnlessEmpty(),
                annotation.indexType.takeUnlessEmpty(),
                annotation.isUnique,
                annotation.properties.map(properties::getStrict),
        )
    }

    descriptor.getAnnotation<PrimaryKey>()?.let { annotation ->
        entity.primaryKey(
            annotation.name.takeUnlessEmpty(),
            annotation.properties.map(properties::getStrict),
        )
    }

    descriptor.getAnnotations<FkReference>()
        .groupBy(FkReference::name)
        .forEach { (name, annotations) ->
            val foreignKey = descriptor.getAnnotations<ForeignKey>()
                .find { annotation -> annotation.name == name }

            entity.foreignKey(
                name.takeUnlessEmpty(),
                annotations.map { annotation ->
                    Tuple3(
                            properties[annotation.property]!!,
                            annotation.targetEntity.toKClass() as KClass<Any>,
                            annotation.targetProperty,
                    )
                },
                foreignKey?.onUpdate?.takeUnlessEmpty(),
                foreignKey?.onDelete?.takeUnlessEmpty(),
            )
        }

    return entity to properties
}
