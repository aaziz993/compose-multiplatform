package klib.data.entity.annotation

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import klib.data.type.collections.sortedByReferences
import klib.data.type.primitives.string.case.toSnakeCase
import klib.data.type.primitives.time.toDuration
import klib.data.type.primitives.time.toInstant
import klib.data.type.primitives.time.toLocalDate
import klib.data.type.primitives.time.toLocalDateTime
import klib.data.type.primitives.time.toLocalTime
import klib.data.type.primitives.toUuid
import klib.data.type.reflection.annotatedTypes
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
                reference.targetTable
            } + descriptor.elementIndices.mapNotNull { index ->
            descriptor.getElementAnnotation<FkReference>(index)?.targetTable
        }).map(String::toKClass)
    }

@Suppress("UNCHECKED_CAST")
public inline fun <T : Any> KClass<*>.toTable(
    table: (name: String) -> T,
    json: T.(columnName: String, KSerializer<Any>, defaultValue: String?) -> Unit,
    jsonb: T.(columnName: String, KSerializer<Any>, defaultValue: String?) -> Unit,
    boolean: T.(columnName: String, defaultValue: Boolean?) -> Unit,
    uByte: T.(columnName: String, defaultValue: UByte?) -> Unit,
    uShort: T.(columnName: String, defaultValue: UShort?) -> Unit,
    uInt: T.(columnName: String, defaultValue: UInt?) -> Unit,
    uLong: T.(columnName: String, defaultValue: ULong?) -> Unit,
    byte: T.(columnName: String, defaultValue: Byte?) -> Unit,
    short: T.(columnName: String, defaultValue: Short?) -> Unit,
    int: T.(columnName: String, defaultValue: Int?) -> Unit,
    long: T.(columnName: String, defaultValue: Long?) -> Unit,
    float: T.(columnName: String, defaultValue: Float?) -> Unit,
    double: T.(columnName: String, defaultValue: Double?) -> Unit,
    decimal: T.(columnName: String, precision: Long, scale: Long, defaultValue: BigDecimal?) -> Unit,
    char: T.(columnName: String, length: Int, collate: String?, defaultValue: String?) -> Unit,
    varchar: T.(columnName: String, length: Int, collate: String?, defaultValue: String?) -> Unit,
    text: T.(columnName: String, collate: String?, eagerLoading: Boolean, defaultValue: String?) -> Unit,
    duration: T.(columnName: String, defaultValue: Duration?) -> Unit,
    instant: T.(columnName: String, defaultValue: Instant?) -> Unit,
    time: T.(columnName: String, defaultValue: LocalTime?) -> Unit,
    date: T.(columnName: String, defaultValue: LocalDate?) -> Unit,
    datetime: T.(columnName: String, defaultValue: LocalDateTime?) -> Unit,
    uuid: T.(columnName: String, defaultValue: Uuid?) -> Unit,
    enum: T.(columnName: String, length: Int?, kClass: KClass<out kotlin.Enum<*>>) -> Unit,
    uByteArray: T.(columnName: String, defaultValue: List<UByte>?) -> Unit,
    uShortArray: T.(columnName: String, defaultValue: List<UShort>?) -> Unit,
    uIntArray: T.(columnName: String, defaultValue: List<UInt>?) -> Unit,
    uLongArray: T.(columnName: String, defaultValue: List<ULong>?) -> Unit,
    binary: T.(columnName: String, length: Int?, defaultValue: ByteArray?) -> Unit,
    blob: T.(columnName: String, useObjectIdentifier: Boolean, defaultValue: ByteArray?) -> Unit,
    shortArray: T.(columnName: String, defaultValue: List<Short>?) -> Unit,
    intArray: T.(columnName: String, defaultValue: List<Int>?) -> Unit,
    longArray: T.(columnName: String, defaultValue: List<Long>?) -> Unit,
    nullable: T.(columnName: String) -> Unit,
    autoIncrement: T.(columnName: String) -> Unit,
    databaseGenerated: T.(columnName: String) -> Unit,
    index: T.(indexName: String?, indexType: String?, isUnique: Boolean, columnNames: Array<String>) -> Unit,
    primaryKey: T.(name: String?, columnNames: Array<String>) -> Unit,
    foreignKey: T. (name: String?, references: List<Tuple3<String, KClass<Any>, String>>, onUpdate: String?, onDelete: String?) -> Unit,
): T {
    val serializer = serializer()
    val descriptor = serializer().descriptor

    val entity = table(
        descriptor.getAnnotation<Entity>()?.name?.takeUnless(String::isEmpty)
            ?: descriptor.serialName.toSnakeCase(),
    )

    descriptor.elementIndices.filterNot { index ->
        descriptor.hasElementAnnotation<Transient>(index)
    }.forEach { index ->
        val elementDescriptor = descriptor.getElementDescriptor(index)
        val column = descriptor.getElementAnnotation<Column>(index)
        val columnName = column?.name ?: descriptor.getElementName(index)
        val defaultValue = descriptor.getElementAnnotation<Default>(index)?.value

        if (descriptor.hasElementAnnotation<Json>(index))
            entity.json(
                columnName,
                serializer.childSerializer(index) as KSerializer<Any>,
                defaultValue,
            )
        else if (descriptor.hasElementAnnotation<Jsonb>(index))
            entity.jsonb(
                columnName,
                serializer.childSerializer(index) as KSerializer<Any>,
                defaultValue,
            )
        else if (elementDescriptor.isEnum) entity.enum(
            columnName,
            descriptor.getElementAnnotation<Enum>(index)?.length,
            elementDescriptor.serialName.toKClass() as KClass<out kotlin.Enum<*>>,
        )
        else when (elementDescriptor.primitiveTypeOrNull?.withNullability(false)) {
            typeOf<Boolean>() -> entity.boolean(columnName, defaultValue?.toBoolean())
            typeOf<UByte>() -> entity.uByte(columnName, defaultValue?.toUByte())
            typeOf<UShort>() -> entity.uShort(columnName, defaultValue?.toUShort())
            typeOf<UInt>() -> entity.uInt(columnName, defaultValue?.toUInt())
            typeOf<ULong>() -> entity.uLong(columnName, defaultValue?.toULong())
            typeOf<Byte>() -> entity.byte(columnName, defaultValue?.toByte())
            typeOf<Short>() -> entity.short(columnName, defaultValue?.toShort())
            typeOf<Int>() -> entity.int(columnName, defaultValue?.toInt())
            typeOf<Long>() -> entity.long(columnName, defaultValue?.toLong())
            typeOf<Float>() -> entity.float(columnName, defaultValue?.toFloat())
            typeOf<Double>() -> entity.double(columnName, defaultValue?.toDouble())
            typeOf<BigDecimal>() ->
                descriptor.getElementAnnotation<Decimal>(index)?.let { annotation ->
                    entity.decimal(
                        columnName,
                        annotation.precision,
                        annotation.scale,
                        defaultValue?.toBigDecimal(),
                    )
                } ?: error("Missing decimal annotation for column '$columnName'")

            typeOf<String>() ->
                descriptor.getElementAnnotation<Char>(index)?.let { annotation ->
                    entity.char(
                        columnName,
                        annotation.length,
                        annotation.collate.takeUnless(String::isEmpty),
                        defaultValue,
                    )
                } ?: descriptor.getElementAnnotation<Varchar>(index)?.let { annotation ->
                    entity.varchar(
                        columnName,
                        annotation.length,
                        annotation.collate.takeUnless(String::isEmpty),
                        defaultValue,
                    )
                } ?: descriptor.getElementAnnotation<Text>(index)?.let { annotation ->
                    entity.text(
                        columnName,
                        annotation.collate,
                        annotation.eagerLoading,
                        defaultValue,
                    )
                } ?: error("Missing string annotation for column '$columnName'")

            typeOf<Duration>() -> entity.duration(
                columnName,
                defaultValue?.toDuration(),
            )

            typeOf<Instant>() -> entity.instant(columnName, defaultValue?.toInstant())
            typeOf<LocalTime>() -> entity.time(columnName, defaultValue?.toLocalTime())
            typeOf<LocalDate>() -> entity.date(columnName, defaultValue?.toLocalDate())
            typeOf<LocalDateTime>() -> entity.datetime(
                columnName,
                defaultValue?.toLocalDateTime(),
            )

            typeOf<Uuid>() -> entity.uuid(columnName, defaultValue?.toUuid())

            else -> when (elementDescriptor.serialName) {
                UByteArray::class.serializer().descriptor.serialName -> entity.uByteArray(
                    columnName,
                    defaultValue?.split(",")?.map(String::toUByte),
                )

                UShortArray::class.serializer().descriptor.serialName -> entity.uShortArray(
                    columnName,
                    defaultValue?.split(",")?.map(String::toUShort),
                )

                UIntArray::class.serializer().descriptor.serialName -> entity.uIntArray(
                    columnName,
                    defaultValue?.split(",")?.map(String::toUInt),
                )

                ULongArray::class.serializer().descriptor.serialName -> entity.uLongArray(
                    columnName,
                    defaultValue?.split(",")?.map(String::toULong),
                )

                ByteArray::class.serializer().descriptor.serialName ->
                    descriptor.getElementAnnotation<Binary>(index)?.let { annotation ->
                        entity.binary(
                            columnName,
                            annotation.length,
                            defaultValue?.encodeToByteArray(),
                        )
                    } ?: descriptor.getElementAnnotation<Blob>(index)?.let { annotation ->
                        entity.blob(
                            columnName,
                            annotation.useObjectIdentifier,
                            defaultValue?.encodeToByteArray(),
                        )
                    } ?: error("Missing bytearray column annotation")

                ShortArray::class.serializer().descriptor.serialName -> entity.shortArray(
                    columnName,
                    defaultValue?.split(",")?.map(String::toShort),
                )

                IntArray::class.serializer().descriptor.serialName -> entity.intArray(
                    columnName,
                    defaultValue?.split(",")?.map(String::toInt),
                )

                LongArray::class.serializer().descriptor.serialName -> entity.longArray(
                    columnName,
                    defaultValue?.split(",")?.map(String::toLong),
                )

                else -> entity.json(
                    columnName,
                    serializer.childSerializer(index) as KSerializer<Any>,
                    defaultValue,
                )
            }
        }

        if (elementDescriptor.isNullable &&
            descriptor.getAnnotation<PrimaryKey>()?.columns?.contains(columnName) != true &&
            !descriptor.hasElementAnnotation<PrimaryKey>(index) &&
            !descriptor.hasElementAnnotation<AutoIncrement>(index) &&
            !descriptor.hasElementAnnotation<DatabaseGenerated>(index)
        ) entity.nullable(columnName)

        descriptor.getElementAnnotation<AutoIncrement>(index)?.let { annotation ->
            entity.autoIncrement(columnName)
        }

        descriptor.getElementAnnotation<DatabaseGenerated>(index)?.let { annotation ->
            entity.databaseGenerated(columnName)
        }

        descriptor.getElementAnnotation<Index>(index)?.let { annotation ->
            entity.index(
                annotation.indexName.takeUnless(String::isEmpty),
                annotation.indexType.takeUnless(String::isEmpty),
                annotation.isUnique,
                arrayOf(columnName, * annotation.columns),
            )
        }

        descriptor.getElementAnnotation<PrimaryKey>(index)?.let { annotation ->
            entity.primaryKey(
                annotation.name.takeUnless(String::isEmpty),
                arrayOf(columnName),
            )
        }

        descriptor.getElementAnnotation<FkReference>(index)?.let { annotation ->
            val foreignKey = descriptor.getElementAnnotation<ForeignKey>(index)

            entity.foreignKey(
                (foreignKey?.name ?: annotation.name).takeUnless(String::isEmpty),
                listOf(
                    Tuple3(
                        columnName,
                        annotation.targetTable.toKClass() as KClass<Any>,
                        annotation.targetColumn,
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
            annotation.columns,
        )
    }

    descriptor.getAnnotation<PrimaryKey>()?.let { annotation ->
        entity.primaryKey(
            annotation.name.takeUnless(String::isEmpty),
            annotation.columns,
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
                        annotation.column,
                        annotation.targetTable.toKClass() as KClass<Any>,
                        annotation.targetColumn,
                    )
                },
                foreignKey?.onUpdate?.takeUnless(String::isEmpty),
                foreignKey?.onDelete?.takeUnless(String::isEmpty),
            )
        }

    return entity
}
