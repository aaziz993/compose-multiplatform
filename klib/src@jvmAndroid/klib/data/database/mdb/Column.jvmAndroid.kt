package klib.data.database.mdb

import com.healthmarketscience.jackcess.ColumnBuilder
import klib.data.database.mdb.complex.ComplexColumnInfo
import klib.data.database.mdb.complex.ComplexValue
import kotlinx.io.IOException

public actual class Column(public val column: com.healthmarketscience.jackcess.Column) {
    public actual constructor(name: String, type: DataType, table: Table) :
        this(ColumnBuilder(name, DATA_TYPE_MAP.inverse[type]!!).addToTable(table.table))

    public actual constructor(name: String, type: DataType, tableDefinition: TableDefinition) :
        this(
            ColumnBuilder(
                name,
                DATA_TYPE_MAP.inverse[type]!!,
            ).addToTableDefinition(tableDefinition.tableDefinition),
        )

    public actual val table: Table
        get() = Table(column.table)

    public actual val database: Database
        get() = Database(column.database)

    public actual val name: String
        get() = column.name

    public actual val isVariableLength: Boolean
        get() = column.isVariableLength

    public actual val isAutoNumber: Boolean
        get() = column.isAutoNumber

    public actual val columnIndex: Int
        get() = column.columnIndex

    public actual val type: DataType
        get() = DATA_TYPE_MAP[column.type]!!

    public actual val isCompressedUnicode: Boolean
        get() = column.isCompressedUnicode

    public actual val precision: Byte
        get() = column.precision

    public actual val scale: Byte
        get() = column.scale

    public actual val length: Short
        get() = column.length

    public actual val lengthInUnits: Short
        get() = column.lengthInUnits

    public actual val isAppendOnly: Boolean
        get() = column.isAppendOnly

    public actual val isHyperlink: Boolean
        get() = column.isHyperlink

    public actual val isCalculated: Boolean
        get() = column.isCalculated

    public actual val complexInfo: ComplexColumnInfo<out ComplexValue>?
        get() = (column.complexInfo as
            com.healthmarketscience.jackcess.complex.ComplexColumnInfo<com.healthmarketscience.jackcess.complex.ComplexValue>?)
            ?.let(::ComplexColumnInfo)

    public actual val properties: PropertyMap
        @Throws(IOException::class)
        get() = PropertyMap(column.properties)

    public actual val versionHistoryColumn: Column
        get() = Column(column.versionHistoryColumn)

    public actual var columnValidator: ColumnValidator?
        get() = JavaColumnValidator(column.columnValidator)
        set(value) {
            column.columnValidator = value?.toColumnValidator()
        }

    public actual fun setRowValue(rowArray: Array<Any?>, value: Any?): Any? = column.setRowValue(rowArray, value)

    public actual fun setRowValue(rowMap: Map<String, Any?>, value: Any?): Any? = column.setRowValue(rowMap, value)

    public actual fun getRowValue(rowArray: Array<Any?>): Any? = column.getRowValue(rowArray)

    public actual fun getRowValue(rowMap: Map<String, *>): Any? = column.getRowValue(rowMap)
}
