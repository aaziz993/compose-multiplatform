package klib.data.db.mdb

import klib.data.db.mdb.complex.ComplexColumnInfo
import klib.data.db.mdb.complex.ComplexValue

public actual class Column() {
    public actual constructor(
        name: String,
        type: DataType,
        table: Table
    ) : this()

    public actual constructor(
        name: String,
        type: DataType,
        tableDefinition: TableDefinition
    ) : this()

    public actual val table: Table
        get() = TODO("Not yet implemented")
    public actual val database: Database
        get() = TODO("Not yet implemented")
    public actual val name: String
        get() = TODO("Not yet implemented")
    public actual val isVariableLength: Boolean
        get() = TODO("Not yet implemented")
    public actual val isAutoNumber: Boolean
        get() = TODO("Not yet implemented")
    public actual val columnIndex: Int
        get() = TODO("Not yet implemented")
    public actual val type: DataType
        get() = TODO("Not yet implemented")
    public actual val isCompressedUnicode: Boolean
        get() = TODO("Not yet implemented")
    public actual val precision: Byte
        get() = TODO("Not yet implemented")
    public actual val scale: Byte
        get() = TODO("Not yet implemented")
    public actual val length: Short
        get() = TODO("Not yet implemented")
    public actual val lengthInUnits: Short
        get() = TODO("Not yet implemented")
    public actual val isAppendOnly: Boolean
        get() = TODO("Not yet implemented")
    public actual val isHyperlink: Boolean
        get() = TODO("Not yet implemented")
    public actual val isCalculated: Boolean
        get() = TODO("Not yet implemented")
    public actual val complexInfo: ComplexColumnInfo<out ComplexValue>?
        get() = TODO("Not yet implemented")
    public actual val properties: PropertyMap
        get() = TODO("Not yet implemented")
    public actual val versionHistoryColumn: Column
        get() = TODO("Not yet implemented")
    public actual var columnValidator: ColumnValidator?
        get() = TODO("Not yet implemented")
        set(value) {}

    public actual fun setRowValue(rowArray: Array<Any?>, value: Any?): Any? {
        TODO("Not yet implemented")
    }

    public actual fun setRowValue(
        rowMap: Map<String, Any?>,
        value: Any?
    ): Any? {
        TODO("Not yet implemented")
    }

    public actual fun getRowValue(rowArray: Array<Any?>): Any? {
        TODO("Not yet implemented")
    }

    public actual fun getRowValue(rowMap: Map<String, *>): Any? {
        TODO("Not yet implemented")
    }
}
