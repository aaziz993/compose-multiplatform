package klib.data.database.mdb

public actual class RowId public actual constructor(pageNumber: Int, rowNumber: Int) :
    Comparable<RowId> {
    public actual val pageNumber: Int
        get() = TODO("Not yet implemented")
    public actual val rowNumber: Int
        get() = TODO("Not yet implemented")
    public actual val type: RowIdType
        get() = TODO("Not yet implemented")

    public actual override operator fun compareTo(other: RowId): Int {
        TODO("Not yet implemented")
    }

    public actual companion object {
        public actual val FIRST_PAGE_NUMBER: Int
            get() = TODO("Not yet implemented")
        public actual val LAST_PAGE_NUMBER: Int
            get() = TODO("Not yet implemented")
        public actual val INVALID_ROW_NUMBER: Int
            get() = TODO("Not yet implemented")
        public actual val FIRST_ROW_ID: RowId
            get() = TODO("Not yet implemented")
        public actual val LAST_ROW_ID: RowId
            get() = TODO("Not yet implemented")
    }
}