@file:Suppress("OPTIONAL_DECLARATION_USAGE_IN_NON_COMMON_SOURCE")

package klib.data.database.mdb

import com.healthmarketscience.jackcess.IndexBuilder
import kotlinx.io.IOException

public actual class Index(private val index: com.healthmarketscience.jackcess.Index) {
    public actual constructor(
        name: String,
        table: Table,
        columnNames: List<String>,
        descendingColumnNames: List<String>
    ) : this(
        IndexBuilder(name)
            .addColumns(*columnNames.toTypedArray())
            .addColumns(false, *descendingColumnNames.toTypedArray())
            .addToTable(table.table)
    )

    public actual constructor(
        name: String, tableDefinition: TableDefinition,
        columnNames: List<String>,
        descendingColumnNames: List<String>
    ) : this(
        IndexBuilder(name)
            .addColumns(*columnNames.toTypedArray())
            .addColumns(false, *descendingColumnNames.toTypedArray())
            .addToTableDefinition(tableDefinition.tableDefinition)
    )

    public actual val table: Table
        get() = Table(index.table)

    public actual val name: String
        get() = index.name

    public actual val isPrimaryKey: Boolean
        get() = index.isPrimaryKey

    public actual val isForeignKey: Boolean
        get() = index.isForeignKey

    public actual val columns: List<Column>
        get() = index.columns.map(::Column)

    public actual val referencedIndex: Index
        @Throws(IOException::class)
        get() = Index(index.referencedIndex)

    public actual val shouldIgnoreNulls: Boolean
        get() = index.shouldIgnoreNulls()

    public actual val isUnique: Boolean
        get() = index.isUnique

    public actual val isRequired: Boolean
        get() = index.isRequired

    public actual class Column(
        public val _column: com.healthmarketscience.jackcess.Index.Column
    ) {
        public actual val column: klib.data.database.mdb.Column
            get() = Column(_column.column)

        public actual val isAscending: Boolean
            get() = _column.isAscending

        public actual val columnIndex: Int
            get() = _column.columnIndex

        public actual val name: String
            get() = _column.name
    }
}
