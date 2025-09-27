package klib.data.database.mdb

public actual class Index() {
    public actual constructor(
        name: String,
        tableDefinition: TableDefinition,
        columnNames: List<String>,
        descendingColumnNames: List<String>
    ) : this() {
        TODO("Not yet implemented")
    }

    public actual constructor(
        name: String,
        table: Table,
        columnNames: List<String>,
        descendingColumnNames: List<String>
    ) : this()

    public actual val table: Table
        get() = TODO("Not yet implemented")
    public actual val name: String
        get() = TODO("Not yet implemented")
    public actual val isPrimaryKey: Boolean
        get() = TODO("Not yet implemented")
    public actual val isForeignKey: Boolean
        get() = TODO("Not yet implemented")
    public actual val columns: List<Column>
        get() = TODO("Not yet implemented")
    public actual val referencedIndex: Index
        get() = TODO("Not yet implemented")
    public actual val shouldIgnoreNulls: Boolean
        get() = TODO("Not yet implemented")
    public actual val isUnique: Boolean
        get() = TODO("Not yet implemented")
    public actual val isRequired: Boolean
        get() = TODO("Not yet implemented")

    public actual class Column {
        public actual val column: klib.data.database.mdb.Column
            get() = TODO("Not yet implemented")
        public actual val isAscending: Boolean
            get() = TODO("Not yet implemented")
        public actual val columnIndex: Int
            get() = TODO("Not yet implemented")
        public actual val name: String
            get() = TODO("Not yet implemented")
    }
}